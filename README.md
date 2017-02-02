# Chip-8 Emulator written in Scala

CHIP-8 Virtual machine implemented in Scala 

## Design

The project is separated into 5 parts:

### Emulator.scala 

Deals with the instance of the emulator and the calling of opcodes from Opcodes.scala.

### Opcodes.scala

Contains all the opcodes of the Chip-8 system.

### Memory.scala 

Deals with loading our Rom into memory.

### Screen.scala 

Contains methods dealing with the screen of our emulator, primarily used in the draw opcode.

### Schip8.scala 

Deals with rendering the emulator to our screen using the Slick2d library aswell as handling keyboard input. Our main function is also contained here.