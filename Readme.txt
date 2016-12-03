Follow the steps to setup the device and the phone:
The device contains an Arduino Board(Leonardo or Micro) a bluetooth module(HC-05, HC-06 ) a button and a resistance.

The pins has to be connected like:
1. Bluetooth RX -> pin 11 Board
2. Bluetooth TX -> pin 10 Board
3. Bluetooth GN -> pin GND Board
4. Bluetooth VCC -> pin 3.3V Board
5. Button VCC -> pin 5V Board
6. Button GND -> pin GND Board
7. Button OUT -> pin 2 Board 

Arduino:
Upload Arduino project using Arduino IDE(or any different way). And after that open a serial monitor to the device. In the serial write master password, it needs to end with the new line. 

After this step just replug the board and is all set.

Android:
First step is to accept all the permissions for Android > 6. After that the user has to go in Settings->Salt and enter a number which represents the salt that is used when the password are encrypted/decrypted. This value has not to be changed. 

After this step the application can be used with the device.
