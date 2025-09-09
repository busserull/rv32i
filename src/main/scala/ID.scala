package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase, SwitchContext, switch, is, Cat, Fill}
import chisel3.experimental.MultiIOModule
import Op1Select.{rs1, PC}
import Op2Select.{rs2, imm}


class InstructionDecode extends MultiIOModule {

  // Don't touch the test harness
  val testHarness = IO(
    new Bundle {
      val registerSetup = Input(new RegisterSetupSignals)
      val registerPeek  = Output(UInt(32.W))

      val testUpdates   = Output(new RegisterUpdates)
    })


  val io = IO(
    new Bundle {
      val inst = Input(new Instruction)
      val pc = Input(UInt(32.W))

      val wb_enable = Input(Bool())
      val wb_address = Input(UInt(5.W))
      val wb_data = Input(UInt(32.W))

      val control = Output(new ControlSignals)
      val rd = Output(UInt(5.W))
      val alu_op = Output(UInt(4.W))
      val op_one = Output(UInt(32.W))
      val op_two = Output(UInt(32.W))
      val target = Output(UInt(32.W))
    }
  )

  val registers = Module(new Registers)
  val decoder   = Module(new Decoder).io

  /**
    * Setup. You should not change this code
    */
  registers.testHarness.setup := testHarness.registerSetup
  testHarness.registerPeek    := registers.io.readData1
  testHarness.testUpdates     := registers.testHarness.testUpdates


  registers.io.readAddress1 := io.inst.registerRs1
  registers.io.readAddress2 := io.inst.registerRs2

  registers.io.writeEnable  := io.wb_enable
  registers.io.writeAddress := io.wb_address
  registers.io.writeData    := io.wb_data

  val rs1 = Wire(UInt(32.W))
  val rs2 = Wire(UInt(32.W))

  rs1 := registers.io.readData1
  rs2 := registers.io.readData2


  decoder.instruction := io.inst

  io.alu_op := decoder.ALUop

  io.rd := io.inst.registerRd
  io.control := decoder.controlSignals

  io.target := registers.io.readData2

  when(decoder.op1Select === Op1Select.rs1){
    io.op_one := registers.io.readData1
  }.elsewhen(decoder.op1Select === Op1Select.PC){
    io.op_one := io.pc
  }.otherwise{
    io.op_one := 0.U(32.W)
  }

  when(decoder.op2Select === Op2Select.rs2){
    io.op_two := registers.io.readData2
  }.elsewhen(decoder.op2Select === Op2Select.imm){
    io.op_two := io.inst.imm(decoder.immType)
  }.otherwise{
    io.op_two := 0.U(32.W)
  }
}
