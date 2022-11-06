import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class BasketballTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    @Test
    public void basketball_valid_PrintScore() {
        Basketball bb = new Basketball();
        bb.print_score();

        Assert.assertEquals("Score: 0 to 0", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void basketball_valid_addpoints_forOpponent() {
        Basketball bb = new Basketball();
        bb.add_points(0, 2);

        Assert.assertEquals("Score: 0 to 2", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void basketball_valid_addpoints_forDartmouth() {
        Basketball bb = new Basketball();
        bb.add_points(1, 2);

        Assert.assertEquals("Score: 2 to 0", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void basketball_invalid_AddPoints() {
        Basketball bb = new Basketball();
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> bb.add_points(2, 2));
    }

    @Test
    public void basketball_invalid_AddPoints2() {
        Basketball bb = new Basketball();
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> bb.add_points(-1, 1));
    }

    @Test
    public void basketball_two_minute_warning_test() {
        Basketball bb = new Basketball();
        bb.two_minute_warning();

        Assert.assertEquals("*** Two minutes left in the game ***", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void basketball_foulShot_with_greater_than_80Percent_printsExpected() {
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.8;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Both shots missed."));
    }

    @Test
    public void basketball_foulShot_with_between_49To75Percent_printsExpected() {
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.6;});
        System.out.print(outputStreamCaptor.toString());
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes one shot and misses one."));
    }
    
    @Test
    public void basketball_foulShot_with_less_than_49percent_48percent_printsExpected() {
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.48;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes both shots."));
    }

    @Test
    public void basketball_foulShot_with_exact_49percent_printsExpected() {
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.49;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes both shots."));
    }

    @Test
    public void basketball_foulShot_with_less_than_75percent_74percent_printsExpected() {
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.74;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes one shot and misses one."));
    }

    @Test
    public void basketball_foulShot_with_exact_75percent_printsExpected() {
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.75;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes one shot and misses one."));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
