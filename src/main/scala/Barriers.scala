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
      val in_reg_write = Input(Bool())
      val in_mem_read = Input(Bool())
      val in_mem_write = Input(Bool())
      val in_branch = Input(Bool())
      val in_jump = Input(Bool())

      val in_alu_op = Input(UInt(4.W))
      val in_op_one = Input(UInt(32.W))
      val in_op_two = Input(UInt(32.W))


      val out_reg_write = Output(Bool())
      val out_mem_read = Output(Bool())
      val out_mem_write = Output(Bool())
      val out_branch = Output(Bool())
      val out_jump = Output(Bool())

      val out_alu_op = Output(UInt(4.W))
      val out_op_one = Output(UInt(32.W))
      val out_op_two = Output(UInt(32.W))
    }
  )

   val reg_write = RegInit(0.U(32.W))
   val mem_read = RegInit(0.U(32.W))
   val mem_write = RegInit(0.U(32.W))
   val branch = RegInit(0.U(32.W))
   val jump = RegInit(0.U(32.W))
   val alu_op = RegInit(0.U(32.W))
   val op_one = RegInit(0.U(32.W))
   val op_two = RegInit(0.U(32.W))

   reg_write := io.in_reg_write
   mem_read := io.in_mem_read
   mem_write := io.in_mem_write
   branch := io.in_branch
   jump := io.in_jump
   alu_op := io.in_alu_op
   op_one := io.in_op_one
   op_two := io.in_op_two

   io.out_reg_write := reg_write
   io.out_mem_read := mem_read
   io.out_mem_write := mem_write
   io.out_branch := branch
   io.out_jump := jump
   io.out_alu_op := alu_op
   io.out_op_one := op_one
   io.out_op_two := op_two
}
