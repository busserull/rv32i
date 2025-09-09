package FiveStage

import chisel3._
import chisel3.core.Input
import chisel3.experimental.MultiIOModule
import chisel3.experimental._
import chisel3.util.Counter


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

  val IFB = Module(new IFBarrier).io
  val IDB = Module(new IDBarrier).io
  val EXB = Module(new EXBarrier).io
  val MEMB = Module(new MEMBarrier).io

  val IF  = Module(new InstructionFetch)
  val ID  = Module(new InstructionDecode)
  val EX  = Module(new Execute)
  val MEM = Module(new MemoryFetch)

  // Try without an explicit WB, instead creating
  // some unholy mix with ID.
  // 
  // Oh boy, what horrors are we creating?
  // 
  // val WB  = Module(new WriteBack)


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
  IDB.in_control := ID.io.control
  IDB.in_rd := ID.io.rd
  IDB.in_alu_op := ID.io.alu_op
  IDB.in_op_one := ID.io.op_one
  IDB.in_op_two := ID.io.op_two

  EX.io.alu_op := IDB.out_alu_op
  EX.io.op_one := IDB.out_op_one
  EX.io.op_two := IDB.out_op_two

  /* EX Barrier */
  EXB.in_control := IDB.out_control
  EXB.in_rd := IDB.out_rd
  EXB.in_result := EX.io.result
  EXB.in_op_two := IDB.out_op_two

  MEM.io.address := EXB.out_result
  MEM.io.data_in := EXB.out_op_two
  MEM.io.mem_read := EXB.out_control.memRead
  MEM.io.mem_write := EXB.out_control.memWrite

  /* MEM Barrier */
  MEMB.in_control := EXB.out_control
  MEMB.in_rd := EXB.out_rd
  MEMB.in_result := EXB.out_result
  MEMB.in_mem := MEM.io.data_out

  /* Write Back */
  ID.io.wb_enable := MEMB.out_control.regWrite
  ID.io.wb_address := MEMB.out_rd
  ID.io.wb_data := MEMB.out_result
}
