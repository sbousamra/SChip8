# Chip-8 Emulator written in Scala

## Design

##### Project separated into 5 parts
####### Emulator.scala deals with the instance of the emulator and the calling of opcodes from Opcodes.scala.
####### Opcodes.scala contains all the opcodes of the Chip-8 system.
####### Memory.scala deals with loading our Rom into memory.
####### Screen.scala contains methods dealing with the screen of our emulator, primarily used in the draw opcode.
####### Schip8.scala deals with rendering the emulator to our screen using the Slick2d library aswell as handling keyboard input. Our main function is also contained here.