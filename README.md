# Connect4

alphabeta_MoodyCasias.java is the file created by my partner and I. 
The rest of the files, which creates the GUI for the game, was supplied by the instructor.

The AI makes decisions by analyzing the board after a particular move, which is done in the function GetChains.
The AI assigns a 'score' to the board after each potential move, which it then adds to the game tree.
The tree is then 'pruned' with the Alpha-beta algorithm.
After time is up, or the tree has been fully evaluated, the AI selects the best move that it found.

To compile the code, a Makefile has been provided, simply type in "make".

To run the AI against a human opponent, add the argument 

  >> -p1 alphabeta_MoodyCasias
  
  Alternatively, replace p1 with p2 and the AI will go second. 
  
  
