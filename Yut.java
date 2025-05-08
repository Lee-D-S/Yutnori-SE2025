import java.util.Random;

public class Yut {
    public YutResult throwRandom() {
    	Random random = new Random();
        int index = random.nextInt(6);
    	
    	switch(index) {
    	case 0: return YutResult.BACKDO;
    	case 1: return YutResult.DO;
    	case 2: return YutResult.GAE;
    	case 3: return YutResult.GEOL;
    	case 4: return YutResult.YUT;
    	case 5: return YutResult.MO;
    	default: throw new RuntimeException("Wrong Yut Result");
    	}		
    }
    void printYutResult() {
		System.out.println(throwRandom());
	}
}
