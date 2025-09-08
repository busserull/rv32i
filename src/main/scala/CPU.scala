package FiveStage

import chisel3._
import chisel3.core.Input
import chisel3.experimental.MultiIOModule
import chisel3.experimental._


class CPU extends MultiIOModule {

  val testHarness = IO(
    new Bundle {
      val setupSignals = Input(new SetupSignals)
      val testReadouts = Output(new TestReadouts)
      val regUpdates   = Output(new RegisterUpdates)
      val memUpdates   = Output(new MemUpdates)
      val currentPC    = Output(UInt(32.W))
    }
  )

  /**
    You need to create the classes for these yourself
    */
  // val IFBarrier  = Module(new IFBarrier).io
  // val IDBarrier  = Module(new IDBarrier).io
  // val EXBarrier  = Module(new EXBarrier).io
  // val MEMBarrier = Module(new MEMBarrier).io
  val IFB = Module(new IFBarrier).io
  val IDB = Module(new IDBarrier).io

  val IF  = Module(new InstructionFetch)
  val ID  = Module(new InstructionDecode)
  val EX  = Module(new Execute)
  val MEM = Module(new MemoryFetch)
  // val WB  = Module(new Execute) (You may not need this one?)


  /**
    * Setup. You should not change this code
    */
  IF.testHarness.IMEMsetup     := testHarness.setupSignals.IMEMsignals
  ID.testHarness.registerSetup := testHarness.setupSignals.registerSignals
  MEM.testHarness.DMEMsetup    := testHarness.setupSignals.DMEMsignals

  testHarness.testReadouts.registerRead := ID.testHarness.registerPeek
  testHarness.testReadouts.DMEMread     := MEM.testHarness.DMEMpeek

  /**
    spying stuff
    */
  testHarness.regUpdates := ID.testHarness.testUpdates
  testHarness.memUpdates := MEM.testHarness.testUpdates
  testHarness.currentPC  := IF.testHarness.PC

  /* IF Barrier */
  IFB.in_pc := IF.io.PC
  IFB.in_inst := IF.io.inst

  ID.io.pc := IFB.out_pc
  ID.io.inst := IFB.out_inst

  /* ID Barrier */
  IDB.in_reg_write := ID.io.reg_write
  IDB.in_mem_read := ID.io.mem_read
  IDB.in_mem_write := ID.io.mem_write
  IDB.in_branch := ID.io.branch
  IDB.in_jump := ID.io.jump
  IDB.in_alu_op := ID.io.alu_op
  IDB.in_op_one := ID.io.op_one
  IDB.in_op_two := ID.io.op_two

  EX.io.alu_op := IDB.out_alu_op
  EX.io.op_one := IDB.out_op_one
  EX.io.op_two := IDB.out_op_two

  /* EX Barrier */
}
