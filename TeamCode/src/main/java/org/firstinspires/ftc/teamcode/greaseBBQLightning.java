package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This is NOT an opmode.
 * This class defines our robot class
 * It contains the robot's hardware configuration
 *
 * Motor channel:  Front Left drive motor:      "myBigMotorFrontLeft"
 * Motor channel:  Front Right drive motor:     "myBigMotorFrontRight"
 * Motor channel:  Back Left drive motor:       "myBigMotorBackLeft"
 * Motor channel:  Back Right drive motor:      "myBigMotorBackRight"
 * Servo channel:  Servo to claw arm base:      "servoPowerArmBase"
 * Servo channel:  Servo to claw arm mid joint: "servoPowerArmMid"
 * Servo channel:  Servo to claw clamp:         "servoPowerArmClaw"
 */
public class greaseBBQLightning
{
    /* Public OpMode members. */
    Gyroscope imu;
    DcMotor myBigMotorFrontLeft = null;
    DcMotor myBigMotorFrontRight = null;
    DcMotor myBigMotorBackLeft = null;
    DcMotor myBigMotorBackRight = null;
    CRServo myBigServoArmBase = null;
    CRServo myBigServoArmMid = null;
    CRServo myBigServoClaw = null;

    double servoPowerArmBase;
    double servoPowerArmMid;
    double servoPowerArmClaw;

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //Function lower claw on top of block
    void dropOnBlock(){
        //Drop Claw onto stone
        myBigServoClaw.setPower(.3);
        sleep(200);
        myBigServoArmMid.setPower(.3);
        sleep(200);
        myBigServoArmBase.setPower(.5);
    }

    //Function Grab stone/Close Claw
    void grabStone() {
        myBigServoClaw.setPower(1);
        sleep(500);
        myBigServoArmBase.setPower(.7);
        myBigServoArmMid.setPower(-.4);
    }

    //Function Return Claw to Start Position
    void returnToStart() {
        myBigServoClaw.setPower(1);
        sleep(500);
        myBigServoArmMid.setPower(0);
        sleep(200);
        myBigServoArmBase.setPower(.25);
        sleep(200);
        myBigServoClaw.setPower(.1);
    }

    /* local OpMode members. */
    private HardwareMap hwMap           =  null;
    //private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    greaseBBQLightning() {
        //servoPowerArmBase = .25;
        //servoPowerArmMid = 0;
        //servoPowerArmClaw = .1;
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and initialize Gyro
        imu = hwMap.get(Gyroscope.class, "imu");

        // Define and initialize all motors
        myBigMotorFrontLeft = hwMap.get(DcMotor.class, "myBigMotorFrontLeft");
        myBigMotorFrontRight = hwMap.get(DcMotor.class, "myBigMotorFrontRight");
        myBigMotorBackLeft = hwMap.get(DcMotor.class, "myBigMotorBackLeft");
        myBigMotorBackRight = hwMap.get(DcMotor.class, "myBigMotorBackRight");

        // Set all motors to zero power
        myBigMotorBackLeft.setPower(0);
        myBigMotorBackRight.setPower(0);
        myBigMotorFrontLeft.setPower(0);
        myBigMotorFrontRight.setPower(0);

        // Set Direction of motors

        myBigMotorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        myBigMotorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        myBigMotorBackRight.setDirection(DcMotor.Direction.FORWARD);
        myBigMotorFrontRight.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize servos
        myBigServoArmBase = hwMap.get(CRServo.class, "myBigServoArmBase");
        myBigServoArmMid = hwMap.get(CRServo.class, "myBigServoArmMid");
        myBigServoClaw = hwMap.get(CRServo.class, "myBigServoClaw");
        myBigServoArmBase.setDirection(CRServo.Direction.REVERSE);

        // Set all servos to initial power
        myBigServoArmBase.setPower(servoPowerArmBase);
        myBigServoArmMid.setPower(servoPowerArmMid);
        myBigServoClaw.setPower(servoPowerArmClaw);

    }
}