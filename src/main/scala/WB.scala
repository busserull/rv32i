package FiveStage
import chisel3._
import chisel3.experimental.MultiIOModule

class WriteBack extends MultiIOModule {
  val test_harness = IO(
    new Bundle {
      val register_setup = Input(new RegisterSetupSignals)
      val register_peek = Output(UInt(32.W))
      val test_updates = Output(new RegisterUpdates)
    }
  )

  val io = IO(
    new Bundle {
      val rd = Input(UInt(32.W))
      val reg_write = Input(Bool())
      val result = Input(UInt(32.W))
    }
  )

  val registers = Module(new Registers)

  registers.testHarness.setup := test_harness.register_setup
  test_harness.register_peek := registers.io.readData1
  test_harness.test_updates := registers.testHarness.testUpdates

  registers.io.readAddress1 := 0.U(32.W)
  registers.io.readAddress2 := 0.U(32.W)

  registers.io.writeAddress := io.rd
  registers.io.writeEnable := io.reg_write
  registers.io.writeData := io.result
}
