
 package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class ShooterSubsystem extends SubsystemBase {

    final SparkMax m_Fly = new SparkMax(Constants.Shooter.m_FlyWheelID, MotorType.kBrushless);
    final SparkMax m_Carr = new SparkMax(Constants.Shooter.m_CarrierID, MotorType.kBrushless);

    public ShooterSubsystem() {
       
        SparkMaxConfig FlyConfig = new SparkMaxConfig();
        FlyConfig.idleMode(IdleMode.kCoast);
        FlyConfig.smartCurrentLimit(Constants.Shooter.FlyCurrentLimit);
        FlyConfig.inverted(Constants.Shooter.m_FlyInvert);

        SparkMaxConfig CarrConfig = new SparkMaxConfig();
        CarrConfig.idleMode(IdleMode.kCoast);
        CarrConfig.smartCurrentLimit(Constants.Shooter.CarrierCurrentLimit);
        CarrConfig.inverted(Constants.Shooter.m_CarrierInvert);

    }


    public Command Shoot (){
        return run(()-> {
    
                m_Fly.set(Constants.Shooter.FLYWHEELSPEEDIN);
                
                m_Carr.set(Constants.Shooter.CARRIERSPEEDOUT);

            });

        }

    public Command ShootReverse (){
            return run(()-> {
    
                m_Fly.set(Constants.Shooter.FLYWHEELSPEEDOUT);
                m_Carr.set(Constants.Shooter.CARRIERSPEEDIN);

            });

        }


    public Command StopShoot (){
        return run(()-> {

            m_Fly.set(0);
            m_Carr.set(0);
        });

    }
} 
    


