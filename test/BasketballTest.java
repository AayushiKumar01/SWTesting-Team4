import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}