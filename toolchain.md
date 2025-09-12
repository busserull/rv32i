# Risc-V 32I toolchain

Clone https://github.com/riscv-collab/riscv-gnu-toolchain.

Next, `mkdir /opt/riscv`, and add `/opt/riscv/bin` to `PATH`.

Then run `docker run -it --rm --mount type=bind,src=.../riscv-gnu-toolchain,dst=.../riscv-gnu-toolchain --mount type=bind,src=/opt/riscv,dst=/opt/riscv`.

Inside the container, run `apt update`, `apt upgrade`, and `apt install autoconf automake autotools-dev curl python3 python3-pip python3-tomli libmpc-dev libmpfr-dev libgmp-dev gawk build-essential bison flex texinfo gperf libtool patchutils bc zlib1g-dev libexpat-dev ninja-build git cmake libglib2.0-dev libslirp-dev`.

Then, from the Risc-V build directory, while in the container, run `./configure --prefix=/opt/riscv --with-arch=rv32i --with-abi=ilp32`.

That's pretty much it.
