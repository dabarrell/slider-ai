# COMP30024 Artificial Intelligence - Project Part B
Semester 1 2017

Group Members
- David Barrell 520704  dbarrell@student.unimelb.edu.au
- Ivan Chee     736901  ichee@student.unimelb.edu.au

## Solution Structure
- SliderBoard.java
  - Abstract class that allows for quickly swapping out internal board representations
- ArrayListSliderBoard.java
  - Extends SliderBoard class
  - Stores ArrayLists of the 3 different piece types
  - Handles all calculations for eval features
- SliderGame.java
  - Represents the game and the rules governing it
  - Contains a class that combines weights and feature values for the eval function
  - Acts as a bridge between game states and game-playing algorithms
  - Key methods
    - getActions() : Possible moves to be made by a player
    - isTerminal() : Ending game state
- SliderState.java
  - Represents the state of the board at a given time
  - Contains method for updating that state, and getting info about that state
  - Stores a clone of the board
  - Has methods for determining the gameplay outcome
    - makeMove() : Applies a move made by a player to the state
    - analyzeUtility() : Updating utility of the current state
    - getHistory() : History of a particular player's moves
- AIPlayer.java
  - Initialises internal board
  - Stores the definitive current state, and a list of previous states
  - Calls TDLeaf algorithm at the end of the game to update weights
- SliderAlphaBetaSearch.java
  - Implementation of an adapted iterative-deepening alpha-beta algorithm
- TDLeaf.java
  - Temporal learning at the end of the game
  - Updates weights based on the state history of a game
- Analysis package
  - For mass generation and analysis of games
  - Generates csv data used in machine learning

## Third Party Libraries
- None used directly, but the search algorithm was adapted from AIMA-Java

## Approach Taken by AI
- Search Strategy
  - Implementation of an iterative-deepening alpha-beta algorithm
    - Tries to make a good decision in limited time
    - Does not rely on the game reaching a terminal state to make a move decision
    - Combines DFS space efficiency and BFS search completeness
  - Maximizing player is Horizontal, minimizing is Vertical
  - Limited to a maximum depth limit (based on available moves) and time-per-move
  - Uses alpha-beta pruning to reduce checking of non-significant branches
  - Order actions based on prioritizing moves towards the player's
    end to maximise the effect of alpha-beta pruning
  - In each iteration, as long as still within the depth limit and time limit:
    - Increments the depth limit
    - Obtains possible moves by the player and sorts them based on value
    - Runs recursive algorithm to determine value of each move
    - As each node is expanded, the previous moves of the player are checked, and the
      branch will be abandoned if
      - Last 3 moves have been the player moving sideways and not forward; or
      - The player's last two moves were opposites
    - At each depth limit, the deepening loop will break if either first move is
      significantly more valuable than the second, or the first move is a safe winner
- Evaluation Function
  - Our evaluation function uses a number of features to determine the value of
    a particular state
    - Moves made towards the player's end
    - Number of opponent's pieces being blocked directly
    - Number of removed pieces
    - Number of unblocked pieces
    - Number of moves made
    - Number of pieces blocking path of an opponent's piece
- Creative Techniques Applied
  - Optimisations as described above:
    - Early exit when safe winner/significantly better first choice
    - Abandoning branch when deemed non-valuable
    - Altering max depth limit based on number of moves available
  - Custom data structures used:
    - Tuple-based storage of move and value/weight and features etc
    - Adaption of circular queue for storing limited move history
  - Machine learning
    - TDLeaf(lambda) used to hone weights over time (described below)
    - Attempted regression training, but not successful

## Machine Learning
- We first looked at training a regressor using state data. To do this we wrote code to generate
  games on mass, and to then analyse those games to output feature data for each state into a
  CSV file to be used for machine learning. We realised the trained model would not be very valuable
  though, as the data used for training it would be from two random players - anything else would
  have taken too much time to generate enough game data.
- We then implemented a TDLeaf algorithm, to run after every game. Using a wrapper class, we ran
  500 games, each time updating the weights. The current weights set in SlideGame.java are the result
  of this. The weights hadn't settled to constant, but the difference between each game was substantially
  reduced.
- A modified Referee was used for both these techniques, and has been included in additional.zip

## Additional Comments
Other features we have experimented with, which may or may not be working.
- Bitboard
  - Reduces space complexity dramatically which would be useful for searching deeper without running out of space as fast
  - Bitwise operations are constant time which improves checking time
  - Max board size of 7x7 meant that including a border to detect off-board moves would be a 9x9 board,
    or a 91-bit number (more than a 'long').
    - Could use BigInteger, but loses efficiency
    - Could write logic to handle with an 8x8 representation, but would be very tedious
- SliderBoard.java
  - Used an interface to facilitate swapping boards out quickly, including the BigInteger bitboard,
    2d array, and current ArrayList implementation. The latter had the best trade-offs in terms of
    memory vs time.
- Monte Carlo Tree Search
  - Run a large number of simulations from the current game state
  - Statistics are kept for each move from the starting state, and the move with the best overall results is returned
  - Consists of four stages
    - Selection
    - Expansion
    - Simulation
    - Back-propagation
- Random Player
  - Plays the game with random moves
  - Moves forward 70% of the time, and sideways 30% of the time
  - Helps with testing against more advanced players
- Human Player
  - Lets us play against our AI
- Packaging
  - All non-required java classes have been submitted in a zip file to allow for easier submission
