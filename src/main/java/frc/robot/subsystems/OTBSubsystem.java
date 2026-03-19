
 package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class OTBSubsystem extends SubsystemBase {

    final SparkMax m_Transfer = new SparkMax(Constants.OTB.m_TransferID, MotorType.kBrushless);
    final SparkMax m_OTB = new SparkMax(Constants.OTB.m_OTBID, MotorType.kBrushless);
    final SparkMax m_Rollers = new SparkMax(Constants.OTB.m_RollersID, MotorType.kBrushless);

    public OTBSubsystem() {
       
        SparkMaxConfig OTBConfig = new SparkMaxConfig();
        OTBConfig.idleMode(IdleMode.kBrake);
        OTBConfig.smartCurrentLimit(Constants.OTB.OTBCurrentLimit);
        OTBConfig.inverted(Constants.OTB.m_OTBInvert);

        SparkMaxConfig TferConfig = new SparkMaxConfig();
        TferConfig.idleMode(IdleMode.kBrake);
        TferConfig.smartCurrentLimit(Constants.OTB.TransferCurrentLimit);
        TferConfig.inverted(Constants.OTB.m_TransferInvert);

        SparkMaxConfig RollersConfig = new SparkMaxConfig();
        RollersConfig.idleMode(IdleMode.kBrake);
        RollersConfig.smartCurrentLimit(Constants.OTB.RollersCurrentLimit);
        RollersConfig.inverted(Constants.OTB.m_RollersInvert);

    }


    public Command DROPOTB (){
        return run(()-> {
    
                m_OTB.set(Constants.OTB.OTBWHEELSPEEDIN);

            });

        }

    public Command OTBUP (){
            return run(()-> {
    
                m_OTB.set(Constants.OTB.OTBWHEELSPEEDOUT);

            });

        }



    public Command StopOTB (){
        return run(()-> {

            m_OTB.set(0);
        });

    }


    public Command TRANSFERIN (){
        return run(()-> {
    
                m_Transfer.set(Constants.OTB.TRANSFERPEEDIN);

            });

        }

    public Command TRANSFEROUT (){
            return run(()-> {
    
                m_Transfer.set(Constants.OTB.TRANSFERSPEEDOUT);

            });

        }


    public Command StopTransfer (){
        return run(()-> {

            m_Transfer.set(0);
        });

    }

        public Command ROLLERSIN (){
        return run(()-> {
    
                m_Rollers.set(Constants.OTB.TRANSFERPEEDIN);

            });

        }

    public Command ROLLERSOUT (){
            return run(()-> {
    
                m_Rollers.set(Constants.OTB.TRANSFERSPEEDOUT);

            });

        }


    public Command StopRollers (){
        return run(()-> {

            m_Rollers.set(0);
        });

    }

} 

    


