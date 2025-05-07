public enum YutResult {

    DO     ( 1, false),
    GAE    ( 2, false),
    GEOL   ( 3, false),
    YUT    ( 4, true ),   // extra turn
    MO     ( 5, true ),   // extra turn
    BACKDO (-1, false);   // move backward

    private final int  steps;
    private final boolean extraTurn;

    YutResult(int steps, boolean extraTurn) {
        this.steps     = steps;
        this.extraTurn = extraTurn;
    }

    public int steps()            { return steps; }
    public boolean extraTurn()    { return extraTurn; }
}