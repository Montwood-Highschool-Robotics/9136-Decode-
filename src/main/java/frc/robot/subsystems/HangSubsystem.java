
 package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class HangSubsystem extends SubsystemBase {

    final SparkMax m_Hang = new SparkMax(Constants.Hang.m_HangWheelID, MotorType.kBrushless);

    public HangSubsystem() {
       
        SparkMaxConfig HangConfig = new SparkMaxConfig();
        HangConfig.idleMode(IdleMode.kBrake);
        HangConfig.smartCurrentLimit(Constants.Hang.HangCurrentLimit);
        HangConfig.inverted(Constants.Hang.m_HangInvert);

    }


    public Command HANGRETRACT (){
        return run(()-> {
    
                m_Hang.set(Constants.Hang.HANGWHEELSPEEDIN);

            });

        }

    public Command HANGRAISE (){
            return run(()-> {
    
                m_Hang.set(Constants.Hang.HANGWHEELSPEEDOUT);

            });

        }



    public Command StopHang (){
        return run(()-> {

            m_Hang.set(0);
        });

    }



} 

    


