// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

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
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;
import swervelib.SwerveInputStream;


import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.OTBSubsystem;
import frc.robot.subsystems.HangSubsystem;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{

  final         CommandPS5Controller driverPS5 = new CommandPS5Controller(0);
  
  final         CommandPS5Controller operatorPs5 = new CommandPS5Controller(2);


  private final POVButton dpadUpButton    = new POVButton(operatorPs5.getHID(), 0);
  private final POVButton dpadRightButton = new POVButton(operatorPs5.getHID(), 90);
  private final POVButton dpadDownButton  = new POVButton(operatorPs5.getHID(), 180);
  private final POVButton dpadLeftButton  = new POVButton(operatorPs5.getHID(), 270);



  
  private final ShooterSubsystem Shooter = new ShooterSubsystem();
  private final OTBSubsystem OTB = new OTBSubsystem();
  private final HangSubsystem Hang = new HangSubsystem();







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
                                                            .robotRelative(true);

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
                                                             .robotRelative(true);

  SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                        () -> -driverPS5.getLeftY(),
                                                                        () -> -driverPS5.getLeftX())
                                                                    .withControllerRotationAxis(() -> driverPS5.getRawAxis(
                                                                        2))
                                                                    .deadband(OperatorConstants.DEADBAND)
                                                                    .scaleTranslation(0.8)
                                                                    .allianceRelativeControl(false);
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
    
 configureBindings();

    // Zero the gyro to correct alliance if needed
    drivebase.zeroGyro();
    DriverStation.silenceJoystickConnectionWarning(true);

    NamedCommands.registerCommand("test", Commands.print("yAA HAA HAA"));
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

    if (RobotBase.isSimulation())
    {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
    } else
    {
      drivebase.setDefaultCommand(driveRobotOrientedAngularVelocity);

      
    }


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

// Shooter
  Shooter.setDefaultCommand(Shooter.StopShoot());

  operatorPs5.button(5) // R1
    .whileTrue(Shooter.Shoot())
    .whileFalse(Shooter.StopShoot());


//Otb

  OTB.setDefaultCommand(OTB.StopOTB());

  operatorPs5.pov(90) // Dpad up
    .whileTrue(OTB.DROPOTB())
    .whileFalse(OTB.StopOTB());

  operatorPs5.pov(180) // Dpad Down
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


// Transfer
  OTB.setDefaultCommand(OTB.StopTransfer());

  operatorPs5.button(4) // TRIANGLE
    .whileTrue(OTB.TRANSFERIN())
    .whileFalse(OTB.StopTransfer());

  operatorPs5.button(3) // CIRCLE
    .whileTrue(OTB.TRANSFEROUT())
    .whileFalse(OTB.StopTransfer());
    
// Hang
  Hang.setDefaultCommand(Hang.StopHang());

  operatorPs5.button(6) // L1
    .whileTrue(Hang.HANGRETRACT())
    .whileFalse(Hang.StopHang());

    

















    }










  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand("New Auto");
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }


}
