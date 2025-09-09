package FiveStage
import chisel3._
import chisel3.util.{SwitchContext, switch, is, Cat, Fill}
import ALUOps._

class ALU extends Module {
  val io = IO(
    new Bundle {
      val alu_op = Input(UInt(4.W))
      val op_one = Input(UInt(32.W))
      val op_two = Input(UInt(32.W))

      val result = Output(UInt(32.W))
    }
  )

  io.result := 0.U(32.W)

  switch(io.alu_op){
    is(ADD){
      io.result := io.op_one + io.op_two
    }
    is(SUB){
      io.result := io.op_one - io.op_two
    }
    is(AND){
      io.result := io.op_one & io.op_two
    }
    is(OR){
      io.result := io.op_one | io.op_two
    }
    is(XOR){
      io.result := io.op_one ^ io.op_two
    }
    is(SLT){
      io.result := io.op_one.asSInt() < io.op_two.asSInt()
    }
    is(SLL){
      io.result := io.op_one << io.op_two(4, 0)
    }
    is(SLTU){
      io.result := io.op_one.asUInt() < io.op_two.asUInt()
    }
    is(SRL){
      io.result := (io.op_one.asUInt()) >> io.op_two
    }
    is(SRA){
      io.result := (io.op_one.asSInt() >> io.op_two(4, 0)).asUInt()
    }
    /*
    is(COPY_A){
      
    }
    is(COPY_B){
      
    }
    */

    /* Not really needed any longer
    is(DC){
      io.result := 0.U(32.W)
    }
    */
  }
}
