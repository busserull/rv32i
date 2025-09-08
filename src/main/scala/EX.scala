package FiveStage
import chisel3._
import chisel3.experimental.MultiIOModule

class Execute extends MultiIOModule {
  val test_harness = IO(
    new Bundle {}
  )

  val io = IO(
    new Bundle {
      val alu_op = Input(UInt(4.W))
      val op_one = Input(UInt(32.W))
      val op_two = Input(UInt(32.W))

      val result = Output(UInt(32.W))
    }
  )

  val alu = Module(new ALU).io

  alu.alu_op := io.alu_op
  alu.op_one := io.op_one
  alu.op_two := io.op_two
  
  io.result := alu.result
}
