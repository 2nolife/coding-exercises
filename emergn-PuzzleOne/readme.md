To whom it may concern,


ATTEMPT-1 (JAVA 6, OOP)
contains the puzzle written with TDD as follows:
first PortionCalculatorTest, then PortionWriterTest, then RunnerTest.

I managed to solve it without using tokenizers, substrings nor string parsing (similar to tokenizers).
The version allows to parse numbers in range from 0 to 99.000.000.000
and the special class allows the range from -99.000.000.000 to 99.000.000.000

Design is simple: 
the CALCULATOR generates sequences of TUPLES and the WRITER converts those into words.



ATTEMPT-2 (SCALA 2.8, FP)
The simple class converting numbers into words.
This is outside of the execrise scope and included for extra credits.



Written in IntelliJ 10

To run please use "mvn test"


27/04/2011