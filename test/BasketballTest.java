import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.mockingDetails;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Basketball.class, Math.class}) // Preparing class under test.
public class BasketballTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void beforeEachSetUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Before
    public void setUpBefore() {
        // Mocking Math.random()
        PowerMockito.mockStatic(Math.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void basketball_valid_PrintScore() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.print_score();

        Assert.assertEquals("Score: 0 to 0", outputStreamCaptor.toString()
                .trim());
        afterEachSetup();
    }

    @Test
    public void basketball_valid_addpoints_forOpponent() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.add_points(0, 2);

        Assert.assertEquals("Score: 0 to 2", outputStreamCaptor.toString()
                .trim());
        afterEachSetup();
    }

    @Test
    public void basketball_valid_addpoints_forDartmouth() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.add_points(1, 2);

        Assert.assertEquals("Score: 2 to 0", outputStreamCaptor.toString()
                .trim());
        afterEachSetup();
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
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.two_minute_warning();

        Assert.assertEquals("*** Two minutes left in the game ***", outputStreamCaptor.toString()
                .trim());
        afterEachSetup();
    }

    @Test
    public void basketball_foulShot_with_greater_than_80Percent_printsExpected() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.8;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Both shots missed."));
        afterEachSetup();
    }

    @Test
    public void basketball_foulShot_with_between_49To75Percent_printsExpected() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.6;});
        System.out.print(outputStreamCaptor.toString());
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes one shot and misses one."));
        afterEachSetup();
    }

    @Test
    public void basketballWithPowerMock_foulShot_with_between_49To75Percent_printsExpected() {
        beforeEachSetUp();
        Mockito.when(Math.random()).thenReturn(0.6);
        Basketball bb = Mockito.spy(new Basketball());
        bb.foul_shots(1);
        Assert.assertTrue(outputStreamCaptor.toString().trim()
                .replace("\n"," ").contains("Shooter makes one shot and misses one"));
        afterEachSetup();
    }
    
    @Test
    public void basketball_foulShot_with_less_than_49percent_48percent_printsExpected() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.48;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes both shots."));
        afterEachSetup();
    }

    @Test
    public void basketball_foulShot_with_exact_49percent_printsExpected() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.49;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes both shots."));
        afterEachSetup();
    }

    @Test
    public void basketball_foulShot_with_less_than_75percent_74percent_printsExpected() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.74;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes one shot and misses one."));
        afterEachSetup();
    }

    @Test
    public void basketball_foulShot_with_exact_75percent_printsExpected() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        bb.foul_shots(1, ()-> {return 0.75;});
        Assert.assertTrue(outputStreamCaptor.toString()
                .trim().contains("Shooter makes one shot and misses one."));
        afterEachSetup();
    }

    @Test
    public void ball_passed_back_test()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();

        bb.ball_passed_back();
        Assert.assertEquals("Ball passed back to you.", outputStreamCaptor.toString().trim());
        Mockito.verify(bb, Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void halftime_test_withRandomMorethan50_callsOpponent()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        Mockito.doNothing().when(bb).opponent_ball();

        Mockito.when(Math.random()).thenReturn(0.7);
        bb.halftime();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Dartmouth controls the tap."));
        Mockito.verify(bb, Mockito.times(1)).dartmouth_ball();
        Mockito.verify(bb, Mockito.times(1)).start_of_period();
        afterEachSetup();
    }

    @Test
    public void halftime_test_withRandomLessthan50_callsOpponent()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        Mockito.doNothing().when(bb).opponent_ball();
        Mockito.when(Math.random()).thenReturn(0.5);
        bb.halftime();

        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("controls the tap."));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).start_of_period();
        afterEachSetup();
    }


    @AfterEach
    public void afterEachSetup() {
        System.setOut(standardOut);
    }
}
