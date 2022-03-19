/*
  2022 everybot code
  written by carson graf
  don't email me, @ me on discord
*/

/*
  This is catastrophically poorly written code for the sake of being easy to follow
  If you know what the word "refactor" means, you should refactor this code
*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import static frc.robot.ArmPosition.UP;
import static frc.robot.ArmPosition.DOWN;

/** Main robot code for the Manzano FRC team 8512's robot. */
public final class Robot extends TimedRobot {

  ///////////// Hardware configuration
  //
  // Motors
  //

  /** The front left drive motor. */
  private final CANSparkMax driveLeftA = new CANSparkMax(1, MotorType.kBrushed);

  /** The rear left drive motor. */
  private final CANSparkMax driveLeftB = new CANSparkMax(2, MotorType.kBrushed);

  /** The front right drive motor. */
  private final CANSparkMax driveRightA = new CANSparkMax(3, MotorType.kBrushed);

  /** The rear right drive motor. */
  private final CANSparkMax driveRightB = new CANSparkMax(4, MotorType.kBrushed);

  /** The arm motor, which lifts the arm up and down. */
  private final CANSparkMax arm = new CANSparkMax(5, MotorType.kBrushless);

  /** The intake motor, which is used to input / output balls.*/
  private final VictorSPX intake = new VictorSPX(6);

  //
  // Controllers
  //

  /**
   * The controller used by the robot's driver. This is used to control the
   * robot's position.
   */
  private final PS4Controller driverController = new PS4Controller(0);

  /**
   * The controller used by the robot's arm operator. This is used to control
   * the arm and the intake.
   */
  private final PS4Controller armController = new PS4Controller(1);


  /**
   * Whether or not the arm is currently lifted in the "up position."
   *
   * This is initialized to "up", because that's how it would start a match.
   */
  ArmPosition armPosition = ArmPosition.UP;

  /** The time at which the arm was instructed to change direction. */
  double lastBurstTime = 0;

  /** The time at which autonomous operation was started. */
  double autoStart = 0;

  /**
   * Whether or not the robot will try to operate autonomously. This can be
   * set via the SmartDashboard on the driver station before the match begins.
   */
  boolean goForAuto = false;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Configure motors to turn correct direction. You may have to invert some of
    // your motors
    driveLeftA.setInverted(true);
    driveLeftA.burnFlash();
    driveLeftB.setInverted(true);
    driveLeftB.burnFlash();
    driveRightA.setInverted(false);
    driveRightA.burnFlash();
    driveRightB.setInverted(false);
    driveRightB.burnFlash();

    arm.setInverted(false);
    arm.setIdleMode(IdleMode.kBrake);
    arm.burnFlash();

    // add a thing on the dashboard to turn off auto if needed
    SmartDashboard.putBoolean("Go For Auto", false);
    goForAuto = SmartDashboard.getBoolean("Go For Auto", false);
  }

  @Override
  public void autonomousInit() {
    // get a time for auton start to do events based on time later
    autoStart = Timer.getFPGATimestamp();
    // check dashboard icon to ensure good to do auto
    goForAuto = SmartDashboard.getBoolean("Go For Auto", false);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // arm control code. same as in teleop
    this.arm.set(getArmSpeed(this.armPosition, this.lastBurstTime));

    // get time since start of auto
    double autoTimeElapsed = Timer.getFPGATimestamp() - autoStart;
    if (goForAuto) {
      // series of timed events making up the flow of auto
      if (autoTimeElapsed < 3) {
        // spit out the ball for three seconds
        intake.set(ControlMode.PercentOutput, -1);
      } else if (autoTimeElapsed < 6) {
        // stop spitting out the ball and drive backwards *slowly* for three seconds
        intake.set(ControlMode.PercentOutput, 0);
        driveLeftA.set(-0.3);
        driveLeftB.set(-0.3);
        driveRightA.set(-0.3);
        driveRightB.set(-0.3);
      } else {
        // do nothing for the rest of auto
        intake.set(ControlMode.PercentOutput, 0);
        driveLeftA.set(0);
        driveLeftB.set(0);
        driveRightA.set(0);
        driveRightB.set(0);
      }
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Set up arcade steer
    double leftDrive = -driverController.getLeftY();
    double rightDrive = -driverController.getRightY();

    double driveLeftPower = leftDrive;
    double driveRightPower = rightDrive;

    driveLeftA.set(driveLeftPower);
    driveLeftB.set(driveLeftPower);
    driveRightA.set(driveRightPower);
    driveRightB.set(driveRightPower);

    // Intake controls
    if (armController.getRawButton(0)) {
      intake.set(VictorSPXControlMode.PercentOutput, 1);
    } else if (armController.getRawButton(2)) {
      intake.set(VictorSPXControlMode.PercentOutput, -1);
    } else {
      intake.set(VictorSPXControlMode.PercentOutput, 0);
    }

    // Arm Controls
    this.arm.set(getArmSpeed(this.armPosition, this.lastBurstTime));

    if (armController.getRawButtonPressed(3) && this.armPosition == DOWN) {
      lastBurstTime = Timer.getFPGATimestamp();
      this.armPosition = UP;
    } else if (armController.getRawButtonPressed(1) && this.armPosition == UP) {
      lastBurstTime = Timer.getFPGATimestamp();
      this.armPosition = DOWN;
    }
  }

  @Override
  public void disabledInit() {
    // On disable turn off everything.
    // done to solve issue with motors "remembering" previous setpoints after
    // reenable
    driveLeftA.set(0);
    driveLeftB.set(0);
    driveRightA.set(0);
    driveRightB.set(0);
    arm.set(0);
    intake.set(ControlMode.PercentOutput, 0);
  }

  // Constants for controlling the arm. consider tuning these for your
  // particular robot
  private static final double ARM_HOLD_UP = 0.08;
  private static final double ARM_HOLD_DOWN = 0.13;
  private static final double ARM_TRAVEL = 0.5;

  private static final double ARM_TIME_UP = 0.5;
  private static final double ARM_TIME_DOWN = 0.35;

  /**
   * Given the currnet position of the arm, and the last time that the arm was
   * instructed to move, determines the speed that the arm should move.
   */
  private static double getArmSpeed(ArmPosition armPosition, double lastBurstTime) {
    if (armPosition == UP) {
      if (Timer.getFPGATimestamp() - lastBurstTime < ARM_TIME_UP) {
        return ARM_TRAVEL;
      }
      return ARM_HOLD_UP;
    }

    if (Timer.getFPGATimestamp() - lastBurstTime < ARM_TIME_DOWN) {
      return -ARM_TRAVEL;
    }
    return -ARM_HOLD_DOWN;
  }
}
