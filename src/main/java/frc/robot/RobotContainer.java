
package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
 import frc.robot.subsystems.swervedrive.Vision.Cameras;

import java.io.File;

import swervelib.SwerveDrive;
import swervelib.SwerveInputStream;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

import java.io.File;
import org.ejml.equation.Variable;
import org.photonvision.PhotonCamera;

import swervelib.SwerveInputStream;
import static edu.wpi.first.units.Units.RPM;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.OTBSubsystem;
// import frc.robot.subsystems.HangSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{



  final         CommandPS5Controller driverPS5 = new CommandPS5Controller(0);
  
  final         CommandPS5Controller operatorPs5 = new CommandPS5Controller(1);


  private final POVButton dpadUpButton    = new POVButton(operatorPs5.getHID(), 0);
  private final POVButton dpadRightButton = new POVButton(operatorPs5.getHID(), 90);
  private final POVButton dpadDownButton  = new POVButton(operatorPs5.getHID(), 180);
  private final POVButton dpadLeftButton  = new POVButton(operatorPs5.getHID(), 270);


  private final PhotonCamera camera = new PhotonCamera("center");
  
  private final ShooterSubsystem Shooter = new ShooterSubsystem();

  private final OTBSubsystem OTB = new OTBSubsystem();
  // private final HangSubsystem Hang = new HangSubsystem();
  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                                "swerve/neo"));

  
  /**
   * Converts driver input into a field-relative ChassisSpeeds that is controlled by angular velocity.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverPS5.getLeftY() * -1,
                                                                () -> driverPS5.getLeftX() * -1)
                                                            .withControllerRotationAxis(driverPS5::getRightX)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);

  /**
   * Clone's the angular velocity input stream and converts it to a fieldRelative input stream.
   */
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverPS5::getRightX,
                                                                                             driverPS5::getRightY)
                                                           .headingWhile(true);
  /**
   * Clone's the angular velocity input stream and converts it to a robotRelative input stream.
   */
  SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                                                             .allianceRelativeControl(false);

  SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                        () -> -driverPS5.getLeftY(),
                                                                        () -> -driverPS5.getLeftX())
                                                                    .withControllerRotationAxis(() -> driverPS5.getRawAxis(
                                                                        2))
                                                                    .deadband(OperatorConstants.DEADBAND)
                                                                    .scaleTranslation(0.8)
                                                                    .allianceRelativeControl(true);
  // Derive the heading axis with math!
  SwerveInputStream driveDirectAngleKeyboard     = driveAngularVelocityKeyboard.copy()
                                                                               .withControllerHeadingAxis(() ->
                                                                                                              Math.sin(
                                                                                                                  driverPS5.getRawAxis(
                                                                                                                      2) *
                                                                                                                  Math.PI) *
                                                                                                              (Math.PI *
                                                                                                               2),
                                                                                                          () ->
                                                                                                              Math.cos(
                                                                                                                  driverPS5.getRawAxis(
                                                                                                                      2) *
                                                                                                                  Math.PI) *
                                                                                                              (Math.PI *
                                                                                                               2))
                                                                               .headingWhile(true)
                                                                               .translationHeadingOffset(true)
                                                                               .translationHeadingOffset(Rotation2d.fromDegrees(
                                                                                   0));

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */

  
  public RobotContainer()
  {
    // Configure the trigger bindings
    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
    NamedCommands.registerCommand("test", Commands.print("Ya Haa Haaa"));
    NamedCommands.registerCommand("DropOTB", OTB.DROPOTB());
    NamedCommands.registerCommand("Rollers", OTB.ROLLERSIN());
    NamedCommands.registerCommand("StopOTB", OTB.StopOTB());
    NamedCommands.registerCommand("StopRollers", OTB.StopRollers());

    NamedCommands.registerCommand("Shoot", Shooter.setVelocity(RPM.of(4000)));
    NamedCommands.registerCommand("Carrier", OTB.Carrier());
    NamedCommands.registerCommand("StopShoot", Shooter.setVelocity(RPM.of(0)));
    NamedCommands.registerCommand("StopCarrier", OTB.StopCarrier());
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {
    Command driveFieldOrientedDirectAngle      = drivebase.driveFieldOriented(driveDirectAngle);
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
    Command driveRobotOrientedAngularVelocity  = drivebase.driveFieldOriented(driveRobotOriented);
    Command driveSetpointGen = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngle);
    Command driveFieldOrientedDirectAngleKeyboard      = drivebase.driveFieldOriented(driveDirectAngleKeyboard);
    Command driveFieldOrientedAnglularVelocityKeyboard = drivebase.driveFieldOriented(driveAngularVelocityKeyboard);
    Command driveSetpointGenKeyboard = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngleKeyboard);

    NamedCommands.registerCommand("AimAtTag",drivebase.aimAtBestTagAutoCommand(camera).withTimeout(1.5));

    if (RobotBase.isSimulation())
    {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
    } else
    {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngle);
    }


    // Trigger limitDetector = new Trigger(() -> Hang.gethangLimit());

    if (Robot.isSimulation())
    {
      Pose2d target = new Pose2d(new Translation2d(1, 4),
                                 Rotation2d.fromDegrees(90));
      drivebase.getSwerveDrive().field.getObject("targetPose").setPose(target);
      driveDirectAngleKeyboard.driveToPose(() -> target,
                                           new ProfiledPIDController(5,
                                                                     0,
                                                                     0,
                                                                     new Constraints(5, 2)),
                                           new ProfiledPIDController(5,
                                                                     0,
                                                                     0,
                                                                     new Constraints(Units.degreesToRadians(360),
                                                                                     Units.degreesToRadians(180))
                                           ));
      driverPS5.create().onTrue(Commands.runOnce(() -> drivebase.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
      driverPS5.button(1).whileTrue(drivebase.sysIdDriveMotorCommand());
      driverPS5.button(2).whileTrue(Commands.runEnd(() -> driveDirectAngleKeyboard.driveToPoseEnabled(true),
                                                     () -> driveDirectAngleKeyboard.driveToPoseEnabled(false)));

     driverPS5.cross().whileTrue(
         drivebase.driveToPose(
             new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))
                             );

    }
    if (DriverStation.isTest())
    {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity); // Overrides drive command above!

    } else
    {

    /*
   * 
   * 
   * 
   *    CONFIGURE CONTROLS
   * 
   * 
   * 
   * 
   */

      driverPS5.cross().onTrue((Commands.runOnce(drivebase::zeroGyro)));

      // driverPS5.square().whileTrue(drivebase.aimAtTagTeleopCommand(driverPS5, CENTER_CAM, 25));
      // driverPS5.square().whileTrue(drivebase.aimAtTagTeleopCommand(driverPS5, CENTER_CAM, 26));

      driverPS5.L1().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      


// AIM YOU FOOL
// Link to the Photonvision client
// http://photonvision.local:5800/#/dashboard 
     
  driverPS5.R1().whileTrue(drivebase.aimAtBestTagTeleopCommand(driverPS5, camera));


    
  
// Shooter


    Shooter.setDefaultCommand(Shooter.setVelocity(RPM.of(0)));
    Shooter.setDefaultCommand(Shooter.setDutyCycle(0));
    Shooter.setDefaultCommand(Shooter.setVelocity2(RPM.of(0)));

    operatorPs5.axisGreaterThan(3, .1) // L2
    .whileTrue(Shooter.setVelocity(RPM.of(4500))); // RPM OF THE FLYWHEEL1 MOTOR MAX 8000 MIN -8000

    operatorPs5.axisGreaterThan(3, .1) // L2
    .whileTrue(Shooter.setVelocity2(RPM.of(4500)));  // RPM OF THE FLYWHEEL2 MOTOR MAX 8000 MIN -8000


    // operatorPs5.button(11).whileTrue(Shooter.setDutyCycle(-1)); //L3

    // operatorPs5.button(13).whileTrue(Shooter.setDutyCycle(1)); //R3


//OTB
  OTB.setDefaultCommand(OTB.StopCarrier());

  operatorPs5.button(5) // L1
    .whileTrue(OTB.Carrier())
    .whileFalse(OTB.StopCarrier());

  OTB.setDefaultCommand(OTB.StopOTB());

  operatorPs5.button(3) // circle
    .whileTrue(OTB.DROPOTB())
    .whileFalse(OTB.StopOTB());



operatorPs5.button(4) // triangle
  .whileTrue(OTB.OTBUP())
  .whileFalse(OTB.StopOTB());

// Rollers
    OTB.setDefaultCommand(OTB.StopRollers());

    operatorPs5.button(2) // Cross
    .whileTrue(OTB.ROLLERSOUT())
    .whileFalse(OTB.StopRollers());

  operatorPs5.button(1) // Square
    .whileTrue(OTB.ROLLERSIN())
    .whileFalse(OTB.StopRollers());




    
// Hang
  // Hang.setDefaultCommand(Hang.StopHang());

  // operatorPs5.button(6) // R1
  //   .whileTrue(Hang.HANGRETRACT())
  //   .whileFalse(Hang.StopHang());


  // operatorPs5.axisGreaterThan(4, 0.1) // R2
  //   .whileTrue(Hang.HANGRAISE())
  //   .whileFalse(Hang.StopHang());


  //   limitDetector.whileTrue(Hang.StopHang());
   }

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand("RightOtto");
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }
}
