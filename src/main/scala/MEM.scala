package FiveStage
import chisel3._
import chisel3.util._
import chisel3.experimental.MultiIOModule


class MemoryFetch() extends MultiIOModule {

  // Don't touch the test harness
  val testHarness = IO(
    new Bundle {
      val DMEMsetup      = Input(new DMEMsetupSignals)
      val DMEMpeek       = Output(UInt(32.W))

      val testUpdates    = Output(new MemUpdates)
    })

  val io = IO(
    new Bundle {
      val address = Input(UInt(32.W))
      val data_in = Input(UInt(32.W))
      val mem_read = Input(Bool())
      val mem_write = Input(Bool())

      val data_out = Output(UInt(32.W))
    })


  val DMEM = Module(new DMEM)


  /**
    * Setup. You should not change this code
    */
  DMEM.testHarness.setup  := testHarness.DMEMsetup
  testHarness.DMEMpeek    := DMEM.io.dataOut
  testHarness.testUpdates := DMEM.testHarness.testUpdates


  DMEM.io.dataAddress := io.address
  DMEM.io.dataIn      := io.data_in
  DMEM.io.writeEnable := io.mem_write

  io.data_out := DMEM.io.dataOut
}
