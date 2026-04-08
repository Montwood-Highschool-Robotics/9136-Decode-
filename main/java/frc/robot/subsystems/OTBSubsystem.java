
 package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class OTBSubsystem extends SubsystemBase {

    final SparkMax m_Transfer = new SparkMax(Constants.OTB.m_TransferID, MotorType.kBrushless);
    final SparkMax m_OTB = new SparkMax(Constants.OTB.m_OTBID, MotorType.kBrushless);

    private final CANBus kCANBus = new CANBus("awesome");
    private final TalonFX m_Carr = new TalonFX(9, kCANBus);

    private final TalonFX m_Roll = new TalonFX(14, kCANBus);

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

        var CarrConfiguration = new TalonFXConfiguration();
        m_Carr.getConfigurator().apply(CarrConfiguration);

        var RollConfiguration = new TalonFXConfiguration();
        m_Roll.getConfigurator().apply(RollConfiguration);
    }


    public Command DROPOTB (){
        return run(()-> {
                m_OTB.set(Constants.OTB.OTBWHEELSPEEDDOWN);

            });

        }

    public Command OTBUP (){
            return run(()-> {
                m_OTB.set(Constants.OTB.OTBWHEELSPEEDUP);

            });

        }



    public Command StopOTB (){
        return run(()-> {

            m_OTB.set(0);
        });

    }



        public Command ROLLERSIN (){
        return run(()-> {
    
                m_Roll.set(Constants.OTB.ROLLERSWHEELSPEEDIN);
            });

        }

    public Command ROLLERSOUT (){
            return run(()-> {
    
                m_Roll.set(Constants.OTB.ROLLERSWHEELSPEEDOUT);
            });

        }


    public Command StopRollers (){
        return run(()-> {

            m_Roll.set(0);
        
        });

    }
public Command Carrier (){
        return run(()-> {
    
                
                
                m_Carr.set(Constants.OTB.CARRIERSPEEDOUT);
                m_Transfer.set(Constants.OTB.TRANSFERPEEDIN);
            });

        }

    public Command CarrierReverse (){
            return run(()-> {
    
                
                m_Carr.set(Constants.OTB.CARRIERSPEEDIN);
                m_Transfer.set(Constants.OTB.TRANSFERSPEEDOUT);
                
            });

        }


    public Command StopCarrier (){
        return run(()-> {

            
            m_Carr.set(0);
            m_Transfer.set(0);
        });
    }

} 

    


