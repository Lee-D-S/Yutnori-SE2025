public enum BoardType {
    SQUARE   { public BoardLayout create() { return new SquareLayout();   } },
    PENTAGON { public BoardLayout create() { return new PentagonLayout(); } },
    HEXAGON  { public BoardLayout create() { return new HexagonLayout();  } };

    public abstract BoardLayout create();
}