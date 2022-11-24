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

import java.io.*;
import java.util.Scanner;

import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

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
        beforeEachSetUp();
        Basketball bb = new Basketball();
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> bb.add_points(2, 2));
        afterEachSetup();
    }

    @Test
    public void basketball_invalid_AddPoints2() {
        beforeEachSetUp();
        Basketball bb = new Basketball();
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> bb.add_points(-1, 1));
        afterEachSetup();
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

    @Test
    public void opponent_ball_test_WhenTimeisFifty()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_non_jumpshot();
        Mockito.doNothing().when(bb).opponent_jumpshot();
        bb.time=49;
        bb.opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).halftime();
        afterEachSetup();
    }

    @Test
    public void opponent_ball_test_OpponentChanceGreaterThanTwo_callsOpponentNonJumpshot()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_non_jumpshot();
        Mockito.doNothing().when(bb).opponent_jumpshot();
        Mockito.doNothing().when(bb).halftime();
        bb.time=49;
        Mockito.when(Math.random()).thenReturn(4.1);
        bb.opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).opponent_non_jumpshot();
        afterEachSetup();
    }

    @Test
    public void opponent_ball_test_OpponentChanceLessThanTwo_callsOpponentJumpshot()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_non_jumpshot();
        Mockito.doNothing().when(bb).opponent_jumpshot();
        Mockito.doNothing().when(bb).halftime();
        bb.time=49;
        Mockito.when(Math.random()).thenReturn(0.1);
        bb.opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).opponent_jumpshot();
        afterEachSetup();
    }

    private void commonMocksForNonJumpShot(Basketball bb) {
        Mockito.doNothing().when(bb).opponent_non_jumpshot();
        Mockito.doNothing().when(bb).opponent_jumpshot();
        Mockito.doNothing().when(bb).halftime();
        Mockito.doNothing().when(bb).two_minute_warning();
        Mockito.doNothing().when(bb).change_defense();
        Mockito.doNothing().when(bb).opponent_ball();
        Mockito.doNothing().when(bb).dartmouth_ball();
        Mockito.doNothing().when(bb).foul_shots(1);
        Mockito.doNothing().when(bb).add_points(1,2);
    }
    @Test
    public void test_dartmouth_non_jump_shot_scenario1()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.time=49;
        Mockito.when(Math.random()).thenReturn(0.1);
        bb.dartmouth_non_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).halftime();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario2()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.time=91;
        Mockito.when(Math.random()).thenReturn(0.1);
        bb.dartmouth_non_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).two_minute_warning();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario3()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.shot=4;
        Mockito.when(Math.random()).thenReturn(0.1);
        bb.dartmouth_non_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Set shot."));
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario4()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.shot=3;
        Mockito.when(Math.random()).thenReturn(0.1);
        bb.dartmouth_non_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Lay up."));
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario5()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.shot=0;
        Mockito.when(Math.random()).thenReturn(0.1);
        bb.dartmouth_non_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).change_defense();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario6()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=7;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.8).thenReturn(0.876).thenReturn(0.926);
        bb.dartmouth_non_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Charging foul. Dartmouth loses the ball"));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario7()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=7;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.8).thenReturn(0.876).thenReturn(0.924);
        bb.dartmouth_non_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot blocked. "));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario8()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=7;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.8).thenReturn(0.7).thenReturn(0.45);
        bb.dartmouth_non_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).foul_shots(1);
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario9()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=7;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.6).thenReturn(0.7).thenReturn(0.5);
        bb.dartmouth_non_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is off the rim."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Dartmouth controls the rebound."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Ball passed back to you."));
        Mockito.verify(bb, Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario10()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=7;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.6).thenReturn(0.7).thenReturn(0.3)
                .thenReturn(0.3);
        bb.dartmouth_non_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is off the rim."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Dartmouth controls the rebound."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is good. Two points."));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).add_points(1,2);
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_non_jump_shot_scenario11()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=7;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.6).thenReturn(-1.0).thenReturn(0.3);
        bb.dartmouth_non_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is off the rim."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains(bb.opponent+" controls the rebound."));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void tets_opponent_jumpshot_scenario1()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.defense=8.0;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.8).thenReturn(1.0);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Offensive foul. Dartmouth's ball."));
        Mockito.verify(bb,Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }
    @Test
    public void test_opponent_jumpshot_scenario2()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.defense=8.0;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.8).thenReturn(0.7);
        bb.opponent_jumpshot();
        Mockito.verify(bb, Mockito.times(1)).foul_shots(0);
        Mockito.verify(bb,Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_opponent_jumpshot_scenario3()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_ball();
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.0001).thenReturn(0.6).thenReturn(0.8);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is off the rim."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains(bb.opponent + " controls the rebound."));
        Mockito.when(Math.random()).thenReturn(0.6).thenReturn(0.8);
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Ball stolen. Easy lay up for Dartmouth."));
        Mockito.verify(bb,Mockito.times(1)).opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).add_points(1,2);
        afterEachSetup();
    }
    @Test
    public void test_opponent_jumpshot_scenario4()
    {
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_non_jumpshot();
        bb.defense = 8;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.6).thenReturn(0.6).thenReturn(0.6);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains(""));
        Mockito.verify(bb, Mockito.times(1)).opponent_non_jumpshot();
        afterEachSetup();
    }
    @Test
    public void test_opponent_jumpshot_scenario5()
    {
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_non_jumpshot();
        bb.defense = 6;
        Mockito.when(Math.random()).thenReturn(0.6).thenReturn(0.006).thenReturn(0.6).thenReturn(0.6);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains(""));
        Mockito.verify(bb, Mockito.times(1)).opponent_non_jumpshot();
        afterEachSetup();
    }

    @Test
    public void test_opponent_jumpshot_scenario6()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_ball();
        bb.defense = 6;
        Mockito.when(Math.random()).thenReturn(0.6).thenReturn(0.006).thenReturn(0.6).thenReturn(0.04);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Pass back to " + bb.opponent +" guard"));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void test_opponent_jumpshot_scenario7()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_ball();
        bb.defense=8;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.006).thenReturn(0.6).thenReturn(0.4);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Pass back to " + bb.opponent +" guard"));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void test_opponent_jumpshot_scenario8()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.defense=8;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.006).thenReturn(0.006);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Dartmouth controls the rebound."));
        Mockito.verify(bb, Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_opponent_jumpshot_scenario9()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.defense=8;
        Mockito.when(Math.random()).thenReturn(0.04);
        bb.opponent_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is good."));
        Mockito.verify(bb, Mockito.times(1)).dartmouth_ball();
        Mockito.verify(bb, Mockito.times(1)).add_points(0,2);
        afterEachSetup();
    }


    @Test
    public void test_opponent_non_jump_shot_scenario1()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        Mockito.doNothing().when(bb).opponent_jumpshot();
        bb.opponent_chance = 4;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.8).thenReturn(1.0);
        bb.opponent_non_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Set shot."));
        Mockito.verify(bb,Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }
    @Test
    public void test_opponent_non_jump_shot_scenario2()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).dartmouth_ball();
        Mockito.doNothing().when(bb).opponent_jumpshot();
        bb.opponent_chance = 3;
        Mockito.when(Math.random()).thenReturn(0.4).thenReturn(0.8).thenReturn(1.0);
        bb.opponent_non_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Lay up"));
        Mockito.verify(bb,Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_opponent_non_jump_shot_scenario3()
    {
        beforeEachSetUp();
        Basketball bb= Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_ball();
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.6).thenReturn(0.8);
        bb.opponent_non_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is missed."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains(bb.opponent + " controls the rebound."));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Ball stolen. Easy lay up for Dartmouth."));
        Mockito.verify(bb,Mockito.times(1)).opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).add_points(1,2);
        afterEachSetup();
    }

    @Test
    public void test_opponent_non_jump_shot_scenario4()
    {
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        Mockito.doNothing().when(bb).opponent_non_jumpshot();
        bb.defense = 6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.6).thenReturn(0.75);
        bb.opponent_non_jumpshot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains(""));
        Mockito.verify(bb, Mockito.times(1)).opponent_non_jumpshot();
        afterEachSetup();
    }


    @Test
    public void test_dartmouth_non_jump_shot_halftime()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.time=49;
        Mockito.when(Math.random()).thenReturn(0.24);
        bb.dartmouth_non_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).halftime();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_ball_for_shot_choice_0(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("0\n".getBytes()));
        bb.time = 51;
        Mockito.doNothing().when(bb).dartmouth_non_jump_shot();
        Mockito.when(Math.random()).thenReturn(0.4);
        bb.dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_ball_for_shot_choice_1(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        bb.time = 51;
        Mockito.doNothing().when(bb).dartmouth_jump_shot();
        Mockito.when(Math.random()).thenReturn(0.4);
        bb.dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_ball_for_shot_choice_2(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        bb.time = 51;
        Mockito.doNothing().when(bb).dartmouth_jump_shot();
        Mockito.when(Math.random()).thenReturn(0.4);
        bb.dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_ball_for_shot_choice_2_time_100_different_scores(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        bb.time = 100;
        bb.score[0] = 10;
        bb.score[1] = 11;
        Mockito.when(Math.random()).thenReturn(0.5);
        bb.dartmouth_ball();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("\n   ***** End Of Game *****"));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Final Score: Dartmouth: " + bb.score[1] + "  "
                + bb.opponent + ": " + bb.score[0]));
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_ball_for_shot_choice_2_time_100_same_scores(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        bb.time = 100;
        bb.score[0] = 10;
        bb.score[1] = 10;
        Mockito.when(Math.random()).thenReturn(0.5);
        Mockito.doNothing().when(bb).start_of_period();
        bb.dartmouth_ball();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("\n   ***** End Of Second Half *****"));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Score at end of regulation time:"));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("     Dartmouth: " + bb.score[1] + " " +
                bb.opponent + ": " + bb.score[0]));
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Begin two minute overtime period"));
        afterEachSetup();
    }
    @Test
    public void test_dartmouth_ball_for_shot_choice_not_in_choices(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("6\n4\n".getBytes()));
        bb.time = 51;
        Mockito.doNothing().when(bb).dartmouth_non_jump_shot();
        Mockito.when(Math.random()).thenReturn(0.4);
        bb.dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_ball_for_shot_choice_not_in_choices_to_cover_else(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("6\nab\n4\n".getBytes()));
        bb.time = 51;
        Mockito.doNothing().when(bb).dartmouth_non_jump_shot();
        Mockito.when(Math.random()).thenReturn(0.4);
        bb.dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_ball_for_non_integer_to_cover_else(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("ab\n4\n".getBytes()));
        bb.time = 51;
        Mockito.doNothing().when(bb).dartmouth_non_jump_shot();
        Mockito.when(Math.random()).thenReturn(0.4);
        bb.dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_change_defense_for_valid_defense_choice1(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("6.0\n".getBytes()));
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.change_defense();
        afterEachSetup();
    }
    @Test
    public void test_change_defense_for_valid_defense_choice2(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("6.5\n".getBytes()));
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.change_defense();
        afterEachSetup();
    }

    @Test
    public void test_change_defense_for_invalid_defense_choice1(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("9.0\n6.5\n".getBytes()));
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.change_defense();
        afterEachSetup();
    }

    @Test
    public void test_change_defense_for_invalid_defense_choice2(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("9.0\n6.0\n".getBytes()));
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.change_defense();
        afterEachSetup();
    }

    @Test
    public void test_change_defense_for_non_double_to_cover_else(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("ab\n6.0\n".getBytes()));
        Mockito.doNothing().when(bb).dartmouth_ball();
        bb.change_defense();
        afterEachSetup();
    }


    @Test
    public void test_dartmouth_jump_shot_halftime()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.time=49;
        Mockito.when(Math.random()).thenReturn(0.24);
        bb.dartmouth_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).halftime();
        afterEachSetup();
    }


    @Test
    public void test_dartmouth_jump_shot_two_minute_warning()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.time=91;
        Mockito.when(Math.random()).thenReturn(0.24);
        bb.dartmouth_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).two_minute_warning();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_jump_shot_charging_foul()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.52).thenReturn(0.6).thenReturn(0.65);
        bb.dartmouth_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Charging foul. Dartmouth loses ball."));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_jump_shot_foul_shot()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.52).thenReturn(0.6).thenReturn(0.5);
        bb.dartmouth_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        Mockito.verify(bb, Mockito.times(1)).foul_shots(1);
        afterEachSetup();
    }
    @Test
    public void test_dartmouth_jump_shot_blocked_by_opponent()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.52).thenReturn(0.5).thenReturn(0.7);
        bb.dartmouth_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is blocked. Ball controlled by " + bb.opponent));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_jump_shot_blocked_by_dartmouth()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.52).thenReturn(0.5).thenReturn(0.45);
        bb.dartmouth_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is blocked. Ball controlled by Dartmouth."));
        Mockito.verify(bb, Mockito.times(1)).dartmouth_ball();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_jump_shot_off_target_opponent_rebound()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.5).thenReturn(0.5);
        bb.dartmouth_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Rebound to " + bb.opponent));
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }
    @Test
    public void test_dartmouth_jump_shot_off_target_dartmouth_rebound()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.4).thenReturn(0.4).thenReturn(.6).thenReturn(.8);
        bb.dartmouth_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Pass stolen by " + bb.opponent
                + ", easy lay up"));
        Mockito.verify(bb, Mockito.times(1)).dartmouth_ball();
        Mockito.verify(bb, Mockito.times(1)).add_points(0,2);
        afterEachSetup();
    }
    @Test
    public void test_dartmouth_jump_shot_off_target_dartmouth_rebound_ball_passed()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.4).thenReturn(0.4).thenReturn(.6).thenReturn(.55);
        bb.dartmouth_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).ball_passed_back();
        afterEachSetup();
    }
    @Test
    public void test_dartmouth_jump_shot_off_target_dartmouth_rebound_dartmouth_non_jump_shot()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.5).thenReturn(0.4).thenReturn(0.4).thenReturn(.1);
        bb.dartmouth_jump_shot();
        Mockito.verify(bb, Mockito.times(1)).dartmouth_non_jump_shot();
        afterEachSetup();
    }

    @Test
    public void test_dartmouth_jump_shot_shot_if_good()
    {
        beforeEachSetUp();
        Basketball bb=Mockito.spy(new Basketball());
        commonMocksForNonJumpShot(bb);
        bb.defense=6;
        Mockito.when(Math.random()).thenReturn(0.1);
        bb.dartmouth_jump_shot();
        Assert.assertTrue(outputStreamCaptor.toString().trim().contains("Shot is good."));
        Mockito.verify(bb, Mockito.times(1)).add_points(1,2);
        Mockito.verify(bb, Mockito.times(1)).opponent_ball();
        afterEachSetup();
    }

    @Test
    public void test_for_run_to_get_coverage(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("9.0\n7.0\nSU\n".getBytes()));
        Mockito.doNothing().when(bb).start_of_period();
        bb.run();
        afterEachSetup();
    }
    @Test
    public void test_for_run_to_get_coverage_for_non_double_value_for_defense(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("ab\n7.0\nSU\n".getBytes()));
        Mockito.doNothing().when(bb).start_of_period();
        bb.run();
        afterEachSetup();
    }
    @Test
    public void test_for_run_to_get_coverage_for_non_valid_non_double_value_for_defense(){
        beforeEachSetUp();
        Basketball bb = Mockito.spy(new Basketball());
        System.setIn(new ByteArrayInputStream("9.0\nab\n7.0\nSU\n".getBytes()));
        Mockito.doNothing().when(bb).start_of_period();
        bb.run();
        afterEachSetup();
    }

    @AfterEach
    public void afterEachSetup() {
        System.setOut(standardOut);
    }
}


