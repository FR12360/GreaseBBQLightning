package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This is NOT an opmode.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class greaseBBQLightning
{
    /* Public OpMode members. */
    public Gyroscope imu;
    public DcMotor myBigMotorFrontLeft = null;
    public DcMotor myBigMotorFrontRight = null;
    public DcMotor myBigMotorBackLeft = null;
    public DcMotor myBigMotorBackRight = null;
    public CRServo myBigServoArmBase = null;
    public CRServo myBigServoArmMid = null;
    public CRServo myBigServoClaw = null;

    public double servoPowerArmBase;
    public double servoPowerArmMid;
    public double servoPowerArmClaw;

    private final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void dropOnBlock(){
        //Drop Claw onto stone
        myBigServoClaw.setPower(.3);
        sleep(200);
        myBigServoArmMid.setPower(.6);
        sleep(200);
        myBigServoArmBase.setPower(.7);
    }

    //Grab stone
    public void grabStone() {
        myBigServoClaw.setPower(1);
        sleep(500);
        myBigServoArmBase.setPower(.7);
        myBigServoArmMid.setPower(-.4);
    }

    //Crane Return to Start Position
    public void returnToStart() {
        myBigServoClaw.setPower(1);
        sleep(500);
        myBigServoArmMid.setPower(0);
        sleep(200);
        myBigServoArmBase.setPower(.25);
        sleep(200);
        myBigServoClaw.setPower(.1);
    }

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public greaseBBQLightning() {
        servoPowerArmBase = .25;
        servoPowerArmMid = 0;
        servoPowerArmClaw = .1;


    }
    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        imu = hwMap.get(Gyroscope.class, "imu");

        // Define and initialize all motors
        myBigMotorFrontLeft = hwMap.get(DcMotor.class, "myBigMotorFrontLeft");
        myBigMotorFrontRight = hwMap.get(DcMotor.class, "myBigMotorFrontRight");
        myBigMotorBackLeft = hwMap.get(DcMotor.class, "myBigMotorBackLeft");
        myBigMotorBackRight = hwMap.get(DcMotor.class, "myBigMotorBackRight");
        ;

        // Set all motors to zero power
        myBigMotorBackLeft.setPower(0);
        myBigMotorBackRight.setPower(0);
        myBigMotorFrontLeft.setPower(0);
        myBigMotorFrontRight.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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


        //while (opModeIsActive()) {
        //tgtPower = this.gamepad1.right_trigger - this.gamepad1.left_trigger;
        //direction = this.gamepad1.left_stick_x * .5;

        //tgtPowerFrontLeftMotor = tgtPower;
        //tgtPowerFrontRightMotor = tgtPower;
        //tgtPowerBackLeftMotor = tgtPower;
        //tgtPowerBackRightMotor = tgtPower;

        //rightTgtPower = -this.gamepad1.right_stick_y;
        //leftTgtPower = this.gamepad1.left_stick_y;

        //tgtPowerFrontLeftMotor = leftTgtPower;
        //tgtPowerFrontRightMotor = rightTgtPower;
        //tgtPowerBackLeftMotor = leftTgtPower;
        //tgtPowerBackRightMotor = rightTgtPower;


            /*if(direction != 0){
                if(tgtPower == 0){
                    //Make Grease Lightning Pivot at full speed
                    direction = this.gamepad1.left_stick_x;
                    if (direction <= 0) {
                        tgtPowerFrontLeftMotor = 1 * direction;
                        tgtPowerBackLeftMotor = 1 * direction;
                        tgtPowerFrontRightMotor = 1 * -direction;
                        tgtPowerBackRightMotor = 1 * -direction;
                    } else if (direction >= 0) {
                        tgtPowerFrontRightMotor = 1 * -direction;
                        tgtPowerBackRightMotor = 1 * -direction;
                        tgtPowerFrontLeftMotor = 1 * direction;
                        tgtPowerBackLeftMotor = 1 * direction;
                    }
                } else if(tgtPower > 0) {

                    direction = this.gamepad1.left_stick_x * .5;

                    if (direction <= 0) {
                        tgtPowerFrontLeftMotor = tgtPower * -direction;
                        tgtPowerBackLeftMotor = tgtPower * -direction;
                    } else if (direction >= 0) {
                        tgtPowerFrontRightMotor = tgtPower * direction;
                        tgtPowerBackRightMotor = tgtPower * direction;
                    }
                }
                else if(tgtPower < 0){

                    direction = this.gamepad1.left_stick_x * .5;

                    if(direction <= 0){
                        tgtPowerFrontLeftMotor = tgtPower * -direction;
                        tgtPowerBackLeftMotor = tgtPower * -direction;
                    } else if(direction >= 0){
                        tgtPowerFrontRightMotor = tgtPower * direction;
                        tgtPowerBackRightMotor = tgtPower * direction;
                    }
                }
            }*/

        //myBigMotorFrontLeft.setPower(tgtPowerFrontLeftMotor);
        //myBigMotorFrontRight.setPower(tgtPowerFrontRightMotor);
        //myBigMotorBackLeft.setPower(tgtPowerBackLeftMotor);
        //myBigMotorBackRight.setPower(tgtPowerBackRightMotor);




        //}





        //Servo Code
        //myBigServoClaw.setPower(this.gamepad1.right_trigger);
        //myBigServoArmMid.setPower(servoPowerArmMid);

        //telemetry.addData("Front Left Motor Power", myBigMotorFrontLeft.getPower());
        //telemetry.addData("Front Right Motor Power", myBigMotorFrontRight.getPower());
        //telemetry.addData("Back Left Motor Power", myBigMotorBackLeft.getPower());
        //telemetry.addData("Back Right Motor Power", myBigMotorBackRight.getPower());
        //telemetry.addData("Direction", myBigServoArmBase.getDirection());
        //telemetry.addData("Arm Base", myBigServoArmBase.getPower());
        //telemetry.addData("Direction", myBigServoArmMid.getDirection());
        //telemetry.addData("Arm Mid", myBigServoArmMid.getPower());
        //telemetry.addData("Direction", myBigServoArmMid.getDirection());
        //telemetry.addData("Arm Claw", myBigServoClaw.getPower());
        //telemetry.addData("Status", "Running");
        //telemetry.update();

        //}