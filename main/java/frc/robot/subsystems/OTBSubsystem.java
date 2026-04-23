
 package frc.robot.subsystems;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import yams.gearing.GearBox;
import yams.gearing.MechanismGearing;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.remote.TalonFXWrapper;

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
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class OTBSubsystem extends SubsystemBase {

    final SparkMax m_Transfer = new SparkMax(14, MotorType.kBrushless);
    final SparkMax m_Transfer2 = new SparkMax(15, MotorType.kBrushless);
    final SparkMax m_OTB = new SparkMax(12, MotorType.kBrushless);

    private final CANBus kCANBus = new CANBus("awesome");
    private final TalonFX m_Carr = new TalonFX(11, kCANBus);

    private final TalonFX m_Roll = new TalonFX(13, kCANBus);


    Orchestra m_orchestra = new Orchestra();
    // Add a single device to the orchestra

    

private final SmartMotorControllerConfig motorConfig = new SmartMotorControllerConfig(this)
      .withClosedLoopController(0.00016541, 0, 0, RPM.of(5000), RotationsPerSecondPerSecond.of(2500))
      .withGearing(new MechanismGearing(GearBox.fromReductionStages(3, 4)))
      .withIdleMode(MotorMode.COAST)
      .withTelemetry("Roller", TelemetryVerbosity.HIGH)
      .withStatorCurrentLimit(Amps.of(40))
      .withMotorInverted(false)
      .withClosedLoopRampRate(Seconds.of(0.25))
      .withOpenLoopRampRate(Seconds.of(0.25))
      .withFeedforward(new SimpleMotorFeedforward(0.27937, 0.089836, 0.014557))
      .withSimFeedforward(new SimpleMotorFeedforward(0.27937, 0.089836, 0.014557))
      .withControlMode(ControlMode.CLOSED_LOOP);

  private final SmartMotorController motor = new TalonFXWrapper(m_Roll, DCMotor.getKrakenX60(1), motorConfig);

  private final FlyWheelConfig RollerConfig = new FlyWheelConfig(motor)
      .withDiameter(Inches.of(4))
      .withMass(Pounds.of(3))
      .withTelemetry("Roller", TelemetryVerbosity.HIGH)
      .withSoftLimit(RPM.of(-8000), RPM.of(8000))
      .withSpeedometerSimulation(RPM.of(8000));
  private final FlyWheel       Roll       = new FlyWheel(RollerConfig);

    public OTBSubsystem() {
       
        SparkMaxConfig OTBConfig = new SparkMaxConfig();
        OTBConfig.idleMode(IdleMode.kBrake);
        OTBConfig.smartCurrentLimit(Constants.OTB.OTBCurrentLimit);
        OTBConfig.inverted(Constants.OTB.m_OTBInvert);

        SparkMaxConfig TferConfig = new SparkMaxConfig();
        TferConfig.idleMode(IdleMode.kBrake);
        TferConfig.smartCurrentLimit(Constants.OTB.TransferCurrentLimit);
        TferConfig.inverted(Constants.OTB.m_TransferInvert);

        SparkMaxConfig TferConfig2 = new SparkMaxConfig();
        TferConfig2.idleMode(IdleMode.kBrake);
        TferConfig2.smartCurrentLimit(Constants.OTB.TransferCurrentLimit);
        TferConfig2.inverted(Constants.OTB.m_TransferInvert2);

        var CarrConfiguration = new TalonFXConfiguration();
        m_Carr.getConfigurator().apply(CarrConfiguration);

        m_orchestra.addInstrument(m_Roll);

        // Attempt to load the chrp
        var status = m_orchestra.loadMusic("output.chrp");

    if (!status.isOK()) {
    // log error
}
    }


    public Command DROPOTB (){
        return run(()-> {
                m_OTB.set(Constants.OTB.OTBWHEELSPEEDDOWN);

            });

        }
    public Command UH (){
        return run(()-> {
                m_orchestra.play();
                Commands.print("Ya Haa Haaa");
            });

        }
        

    public Command OTBUP (){
            return run(()-> {
                m_OTB.set(Constants.OTB.OTBWHEELSPEEDUP);

            });

        }
            public Command OTBUPBUTSLOW (){
            return run(()-> {
                m_OTB.set(.2);

            });

        }



    public Command StopOTB (){
        return run(()-> {

            m_OTB.set(0);
        });

    }





        public Command setVelocity(AngularVelocity speed) {
        return Roll.setSpeed(speed);
        }



public Command Carrier (){
        return run(()-> {
    
                
                
                m_Carr.set(Constants.OTB.CARRIERSPEEDOUT);
                m_Transfer.set(Constants.OTB.TRANSFERPEEDIN);
                m_Transfer2.set(Constants.OTB.TRANSFERPEEDIN * -1);
            });

        }

    public Command CarrierReverse (){
            return run(()-> {
    
                
                m_Carr.set(Constants.OTB.CARRIERSPEEDIN);
                m_Transfer.set(Constants.OTB.TRANSFERSPEEDOUT);
                m_Transfer2.set(Constants.OTB.TRANSFERSPEEDOUT * -1);
                
            });

        }


    public Command StopCarrier (){
        return run(()-> {

            
            m_Carr.set(0);
            m_Transfer.set(0);
            m_Transfer2.set(0);
        });
    }

} 

    


