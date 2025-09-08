package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase, SwitchContext, switch, is }
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

  val raw_inst = Wire(UInt(32.W))
  raw_inst := io.inst.asUInt()


  registers.io.readAddress1 := raw_inst(19, 15)
  registers.io.readAddress2 := raw_inst(24, 20)

  registers.io.writeEnable  := io.wb_enable
  registers.io.writeAddress := io.wb_address
  registers.io.writeData    := io.wb_data

  val rs1 = Wire(UInt(32.W))
  val rs2 = Wire(UInt(32.W))

  rs1 := registers.io.readData1
  rs2 := registers.io.readData2


  decoder.instruction := io.inst

  io.alu_op := decoder.ALUop
  io.op_one := 0.U(32.W)
  io.op_two := 0.U(32.W)

  io.rd := raw_inst(11, 7)
  io.control := decoder.controlSignals

  switch(decoder.op1Select){
    is(rs1){
      io.op_one := registers.io.readData1
    }
    is(PC){
      io.op_one := io.pc
    }
  }

  switch(decoder.op2Select){
    is(rs2){
      io.op_two := registers.io.readData2
    }
    is(imm){
      io.op_two := raw_inst(31, 20)
    }
  }

  /*
  when(decoder.op1Select === Op1Select.rs1){
    io.op_one := registers.io.readData1
  }.elsewhen(decoder.op1Select === Op1Select.PC){
    io.op_one := io.pc
  }.otherwise{
    io.op_one := 0.U(32.W)
  }
  */

  /*
  switch(decoder.op1Select){
    is(Op1Select.rs1){
      io.op_one := registers.io.readData1
    }
    is(Op1Select.PC){
      io.op_one := io.pc
    }
    is(Op1Select.DC){
      io.op_one := 0.U(32.W)
    }
  }
  */

  /*
  when(decoder.op2Select === Op2Select.rs2){
    io.op_two := registers.io.readData2
  }.elsewhen(decoder.op2Select === Op2Select.imm){
    io.op_two := raw_inst(31, 20)
  }.otherwise{
    io.op_two := 0.U(32.W)
  }
  */

  /*
  switch(decoder.op2Select){
    is(Op2Select.rs2){
      io.op_two := registers.io.readData2
    }
    is(Op2Select.imm){
      io.op_two := raw_inst(31, 20)
    }
    is(Op2Select.DC){
      io.op_two := 0.U(32.W)
    }
  }
  */
}
