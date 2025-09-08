package FiveStage
import chisel3._
import chisel3.util.{SwitchContext, switch, is}
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
    /* TODO:
    is(SLT){
      
    }
    is(SLL){
      
    }
    is(SLTU){
      
    }
    is(SRL){
      
    }
    is(SRA){
      
    }
    is(COPY_A){
      
    }
    is(COPY_B){
      
    }
    */
    is(DC){
      io.result := 0.U(32.W)
    }
  }
}
