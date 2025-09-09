package FiveStage
import chisel3._

class IFBarrier extends Module {
  val io = IO(
    new Bundle {
      val in_inst = Input(new Instruction)
      val in_pc = Input(UInt(32.W))

      val out_inst = Output(new Instruction)
      val out_pc = Output(UInt(32.W))
    }
  )

  val pc = RegInit(0.U(32.W))

  pc := io.in_pc
  io.out_pc := pc

  io.out_inst := io.in_inst
}

class IDBarrier extends Module {
  val io = IO(
    new Bundle {
      val in_control = Input(new ControlSignals)
      val in_rd = Input(UInt(5.W))
      val in_alu_op = Input(UInt(4.W))
      val in_op_one = Input(UInt(32.W))
      val in_op_two = Input(UInt(32.W))

      val out_control = Output(new ControlSignals)
      val out_rd = Output(UInt(5.W))
      val out_alu_op = Output(UInt(4.W))
      val out_op_one = Output(UInt(32.W))
      val out_op_two = Output(UInt(32.W))
    }
  )

   io.out_control := io.in_control
   io.out_rd := io.in_rd
   io.out_alu_op := io.in_alu_op
   io.out_op_one := io.in_op_one
   io.out_op_two := io.in_op_two

   /*
   val control = Reg(new ControlSignals)
   val rd = RegInit(0.U(5.W))
   val alu_op = RegInit(0.U(32.W))
   val op_one = RegInit(0.U(32.W))
   val op_two = RegInit(0.U(32.W))

   control := io.in_control
   rd := io.in_rd
   alu_op := io.in_alu_op
   op_one := io.in_op_one
   op_two := io.in_op_two

   io.out_control := control
   io.out_rd := rd
   io.out_alu_op := alu_op
   io.out_op_one := op_one
   io.out_op_two := op_two
   */
}

class EXBarrier extends Module {
  val io = IO(
    new Bundle {
      val in_control = Input(new ControlSignals)
      val in_rd = Input(UInt(5.W))
      val in_result = Input(UInt(32.W))

      val out_control = Output(new ControlSignals)
      val out_rd = Output(UInt(5.W))
      val out_result = Output(UInt(32.W))
    }
  )

   io.out_control := io.in_control
   io.out_rd := io.in_rd
   io.out_result := io.in_result

   /*
   val control = Reg(new ControlSignals)
   val rd = RegInit(0.U(5.W))
   val result = RegInit(0.U(32.W))

   control := io.in_control
   rd := io.in_rd
   result := io.in_result

   io.out_control := control
   io.out_rd := rd
   io.out_result := result
   */
}

class MEMBarrier extends Module {
  val io = IO(
    new Bundle {
      val in_control = Input(new ControlSignals)
      val in_rd = Input(UInt(5.W))
      val in_result = Input(UInt(32.W))

      val out_control = Output(new ControlSignals)
      val out_rd = Output(UInt(5.W))
      val out_result = Output(UInt(32.W))
    }
  )

  io.out_control := io.in_control
  io.out_rd := io.in_rd
  io.out_result := io.in_result

   /*
   val control = Reg(new ControlSignals)
   val rd = RegInit(0.U(32.W))
   val result = RegInit(0.U(32.W))

   control := io.in_control
   rd := io.in_rd
   result := io.in_result

   io.out_control := control
   io.out_rd := rd
   io.out_result := result
   */
}
