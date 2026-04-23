// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean constants. This
 * class should not be used for any other purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{

  public static final double ROBOT_MASS = (116 - 12) * 0.453592; // 32lbs * kg per pound
  public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag
  public static final double MAX_SPEED  = Units.feetToMeters(14.5);
  public static final double MAX_ANGULAR_VELOCITY = 10.0;
  // Maximum speed of the robot in meters per second, used to limit acceleration.


  public static final class DrivebaseConstants
  {

    // Hold time on motor brakes when disabled
    public static final double WHEEL_LOCK_TIME = 10; // seconds
  }

  public static class OperatorConstants
  {

    // Joystick Deadband
    public static final double DEADBAND        = 0.1;
    public static final double LEFT_Y_DEADBAND = 0.1;
    public static final double RIGHT_X_DEADBAND = 0.1;
    public static final double TURN_CONSTANT    = 6;
  }

  
  public static class Shooter // Shooter1 Chud Shooter2 Jack 

  {


    public static int m_FlyWheelID = 10;
    public static int m_FlyWheelID2 = 10;
    public static int FlyCurrentLimit = 60;
    public static boolean m_FlyInvert = false;
    public static double FLYWHEELSPEEDOUT = .9;
    public static double FLYWHEELSPEEDIN = -1;

  }


  public static class OTB
  {
    // 
    public static int m_TransferID = 13;
    public static int TransferCurrentLimit = 20;
    public static boolean m_TransferInvert = false;
    public static boolean m_TransferInvert2 = true;
    public static double TRANSFERPEEDIN = .5; //.5
    public static double TRANSFERSPEEDOUT = -.5;//-.5
    // The The Lifter
    public static int m_OTBID = 12; 
    public static int OTBCurrentLimit = 20;
    public static boolean m_OTBInvert = false;
    public static double OTBWHEELSPEEDDOWN = -.3;
    public static double OTBWHEELSPEEDUP = .3;
    // Jose Newfeild
    public static int m_RollersID = 14; 
    public static int RollersCurrentLimit = 30;
    public static boolean m_RollersInvert = false;
    public static double ROLLERSWHEELSPEEDOUT = -1;
    public static double ROLLERSWHEELSPEEDIN = 1;

    // Ungaloid
    public static int m_CarrierID = 9;
    public static int CarrierCurrentLimit = 30;
    public static boolean m_CarrierInvert = false;
    public static double CARRIERSPEEDIN = 1;
    public static double CARRIERSPEEDOUT = -1;

  }


    public static class Hang
  {

    public static int m_HangWheelID = 11;
    public static int HangCurrentLimit = 20;
    public static boolean m_HangInvert = false;
    public static double HANGWHEELSPEEDOUT = 1;
    public static double HANGWHEELSPEEDIN = -1;
  }
}
