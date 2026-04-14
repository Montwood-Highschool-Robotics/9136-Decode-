
//  package frc.robot.subsystems;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.Constants;

// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.SparkLowLevel.MotorType;

// import com.revrobotics.spark.config.SparkMaxConfig;
// import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


// public class ShooterSubsystem extends SubsystemBase {

//     final SparkMax m_Fly = new SparkMax(Constants.Shooter.m_FlyWheelID, MotorType.kBrushless);
    

//     public ShooterSubsystem() {
       
//         SparkMaxConfig FlyConfig = new SparkMaxConfig();
//         FlyConfig.idleMode(IdleMode.kCoast);
//         FlyConfig.smartCurrentLimit(Constants.Shooter.FlyCurrentLimit);
//         FlyConfig.inverted(Constants.Shooter.m_FlyInvert);
//         m_Fly.getAbsoluteEncoder();






        
//     }


//     public Command Shoot (){
//         return run(()-> {
    
//                 m_Fly.set(Constants.Shooter.FLYWHEELSPEEDIN);
                
                

//             });

//         }

//     public Command ShootReverse (){
//             return run(()-> {
    
//                 m_Fly.set(Constants.Shooter.FLYWHEELSPEEDOUT);
            
                
//             });

//         }


//     public Command StopShoot (){
//         return run(()-> {

//             m_Fly.set(0);
            
//         });
//     }

// }
    


package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.DegreesPerSecondPerSecond;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;
import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;
import static edu.wpi.first.units.Units.VoltsPerRadianPerSecond;
import static yams.mechanisms.SmartMechanism.gearbox;
import static yams.mechanisms.SmartMechanism.gearing;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;
import yams.motorcontrollers.remote.TalonFXSWrapper;
import yams.motorcontrollers.remote.TalonFXWrapper;

public class ShooterSubsystem extends SubsystemBase
{
  // TODO: Add detailed comments explaining the example, similar to the ExponentiallyProfiledArmSubsystem
  private final CANBus kCANBus = new CANBus("awesome");
  private final TalonFX ShooterMotor = new TalonFX(10, kCANBus);
  private final TalonFX ShooterMotor2 = new TalonFX(9, kCANBus);

  private final SmartMotorControllerConfig motorConfig = new SmartMotorControllerConfig(this)
      .withClosedLoopController(0.00016541, 0, 0, RPM.of(5000), RotationsPerSecondPerSecond.of(2500))
      .withGearing(new MechanismGearing(GearBox.fromReductionStages(3, 4)))
      .withIdleMode(MotorMode.COAST)
      .withTelemetry("ShooterMotor", TelemetryVerbosity.HIGH)
      .withStatorCurrentLimit(Amps.of(60))
      .withMotorInverted(false)
      .withClosedLoopRampRate(Seconds.of(0.25))
      .withOpenLoopRampRate(Seconds.of(0.25))
      .withFeedforward(new SimpleMotorFeedforward(0.27937, 0.089836, 0.014557))
      .withSimFeedforward(new SimpleMotorFeedforward(0.27937, 0.089836, 0.014557))
      .withControlMode(ControlMode.CLOSED_LOOP);

        private final SmartMotorControllerConfig motorConfig2 = new SmartMotorControllerConfig(this)
      .withClosedLoopController(0.00016541, 0, 0, RPM.of(5000), RotationsPerSecondPerSecond.of(2500))
      .withGearing(new MechanismGearing(GearBox.fromReductionStages(3, 4)))
      .withIdleMode(MotorMode.COAST)
      .withTelemetry("ShooterMotor2", TelemetryVerbosity.HIGH)
      .withStatorCurrentLimit(Amps.of(60))
      .withMotorInverted(true)
      .withClosedLoopRampRate(Seconds.of(0.25))
      .withOpenLoopRampRate(Seconds.of(0.25))
      .withFeedforward(new SimpleMotorFeedforward(0.27937, 0.089836, 0.014557))
      .withSimFeedforward(new SimpleMotorFeedforward(0.27937, 0.089836, 0.014557))
      .withControlMode(ControlMode.CLOSED_LOOP);

      


  private final SmartMotorController motor = new TalonFXWrapper(ShooterMotor, DCMotor.getKrakenX60(1), motorConfig);
  private final SmartMotorController motor2 = new TalonFXWrapper(ShooterMotor2, DCMotor.getKrakenX60(1), motorConfig2);

  private final FlyWheelConfig shooterConfig = new FlyWheelConfig(motor)
      .withDiameter(Inches.of(4))
      .withMass(Pounds.of(3))
      .withTelemetry("ShooterMech", TelemetryVerbosity.HIGH)
      .withSoftLimit(RPM.of(-8000), RPM.of(8000))
      .withSpeedometerSimulation(RPM.of(8000));
  private final FlyWheel       shooter1       = new FlyWheel(shooterConfig);

    private final FlyWheelConfig shooterConfig2 = new FlyWheelConfig(motor2)
      .withDiameter(Inches.of(4))
      .withMass(Pounds.of(3))
      .withTelemetry("ShooterMech", TelemetryVerbosity.HIGH)
      .withSoftLimit(RPM.of(-8000), RPM.of(8000))
      .withSpeedometerSimulation(RPM.of(8000));
  private final FlyWheel       shooter2       = new FlyWheel(shooterConfig2);

  public ShooterSubsystem() {}

  public AngularVelocity getVelocity() {
    return shooter1.getSpeed();
  }

  public Command setVelocity(AngularVelocity speed) {
    return shooter1.setSpeed(speed);
  }

  public Command setVelocity2(AngularVelocity speed) {
    return shooter2.setSpeed(speed);
  }

  public Command setDutyCycle(double dutyCycle) {
    return shooter1.set(dutyCycle);
  }

  public Command setVelocity(Supplier<AngularVelocity> speed) {
    return shooter1.setSpeed(speed);
  }

  public Command setDutyCycle(Supplier<Double> dutyCycle) {
    return shooter1.set(dutyCycle);
  }

  public Command sysId() {
    return shooter1.sysId(Volts.of(10), Volts.of(1).per(Second), Seconds.of(5));
  }

  @Override
  public void periodic() {
      shooter1.updateTelemetry();
      shooter2.updateTelemetry();
  }

  @Override
  public void simulationPeriodic() {
      shooter1.simIterate();
      shooter1.simIterate();
  }
}