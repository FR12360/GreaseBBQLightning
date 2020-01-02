/**This is essentially the folder where all your code files for
 * your robot will reside. As of now for FTC 2020, all code files
 * must reside in the "teamcode" folder. If you look at your folder
 * structure most likely on the left hand side of android studio,
 * it should look something like below:
 *
 * ProjectName (Whatever you named your project when you created it for the first time)
 * |--.gradle
 * |--.idea
 * |--doc
 * |--FtcRobotController
 * |--gradle
 * |--libs
 * |+-- TeamCode
 * |    |--build
 * |    |+--src
 * |    |   |+--main
 * |    |   |   |+--java
 * |    |   |   |   |+--org <----THE PACKAGE FOLDER LOCATION BEGINS HERE UNDER THE "JAVA" FOLDER
 * |    |   |   |   |   |+--firstinspires
 * |    |   |   |   |   |   |+--ftc
 * |    |   |   |   |   |   |   |+--teamcode
 * |    |   |   |   |   |   |   |   |--file1.java <----These are your files containing all your
 * |    |   |   |   |   |   |   |   |--file2.java       code.
 * |    |   |   |   |   |   |   |   |--file3.java
 *
 *                                      ---------
 *                  |org.                       |
 *                      |firstinspires.         | org.firstinspires.ftc.teamcode
 *                          |ftc.               |
 *                              |teamcode       |
 *                                      ---------
 *
 * This is an object type "package". A package is simply a portion of the entire application.
 * Files (also referred to as classes) inside each package can only reference
 * each other for the most part. External libraries or packages can be imported as you will see
 * in the next section.
*/
package org.firstinspires.ftc.teamcode;

/**
 * Below are the imported libraries. These libraries contain code that has already been written
 * and compiled for use by FTC. As you can see, there are libraries for every hardware type
 * allowed, so before we can begin to declare, modify, and control our hardware, such as our motors,
 * we need to make sure we import the necessary library that contains all the FTC code. In the case
 * of motor control, we would need to import the DcMotor library which can be referenced with the
 * path "com.qualcomm.robotcore.hardware.DcMotor". The path to reference these precompiled libraries
 * is usually given to use in a manual or in the sample code provided. These libraries are stored in
 * the "libs" folder in the root(main) folder of your project.
 */

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple; May be redundant with DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This is NOT an opmode.
 * This class defines our robot class.
 * It contains the robot's hardware configuration.
 * We will reference this class in many of our other classes.
 * The reason for creating an "object class" like this one is to organize and group together
 * similar objects which will help our code stay organized. Also, if we need to use our robot class
 * in multiple classes, we won't have to write all of this code again. Last but not least, if we
 * change any of the hardware on our robot, we only need to update this object class instead of
 * having to find and update all the classes that uses our robot's hardware. Below we will keep
 * an updated list of the hardware on our robot along with the last date it was updated and by who.
 *
 * Last Updated: 11/27/2019 by MJ
 * --------------------------------------------------------------------
 * -----------------------Wheel Motors---------------------------------
 * --------------------------------------------------------------------
 * Rev Exp Hub 3 Motor channel 0: RC Config Name: "myBigMotorFrontLeft"
 * Rev Exp Hub 3 Motor channel 1: RC Config Name: "myBigMotorFrontRight"
 * Rev Exp Hub 3 Motor channel 2: RC Config Name: "myBigMotorBackLeft"
 * Rev Exp Hub 3 Motor channel 3: RC Config Name: "myBigMotorBackRight"
 *
 * --------------------------------------------------------------------
 * -----------------------Other Motors---------------------------------
 * --------------------------------------------------------------------
 * Rev Exp Hub 2 Motor channel 0: RC Config Name: "myBigMotorRandP"
 * Rev Exp Hub 2 Motor channel 1: RC Config Name: "myBigMotorRandP"
 * Rev Exp Hub 2 Motor channel 2: RC Config Name: "myBigMotorRandP"
 * --------------------------------------------------------------------
 * ----------------------------Servos----------------------------------
 * --------------------------------------------------------------------
 * Rev Exp Hub 3 Servo channel 2: RC Config Name: "myBigServoLeftFoundation"
 * Rev Exp Hub 3 Servo channel 3: RC Config Name: "myBigServoRightFoundation"
 * Rev Exp Hub 3 Servo channel 4: RC Config Name: "myBigMotorLeftLifter" --Not Connected/Used For Testing
 * Rev Exp Hub 3 Servo channel 5: RC Config Name: "myBigMotorRightLifter" --Not Connected/Used For Testing
 */

/**
 * This is where we begin our coding. First, we declare our robot object class. We decided to name
 * our object greaseBBQLightning after the name of the robot. "public class" means that this code
 * is accessible to all other classes inside our "teamcode" package.
 */
public class greaseBBQLightning
{
    /**
     * Next, we declare all of our "public" object class members. In this case, we are declaring
     * all the hardware that we want accessible by the other classes in our package to be able to
     * modify and control. In our case, we want the other classes to be able to change things like
     * speed and direction of all our motors and servos. We also make our built-in gyroscope public
     * just in case we decide to use it. (We aren't at the moment)
     *
     * So to declare(create) our members, first we decide what type of hardware it is. For example:
     * "DcMotor myBigMotorFrontLeft = null;" DcMotor is the member type followed by whatever name we
     * decide to give it. To make things easier, we named the member the same as what it is named
     * in the robot controller configuration so that there is no confusion "myBigMotorFrontLeft".
     * Lastly, we set it equal to "null" which means we set it equal to nothing......for now.
     * Think of it as a temporary shell that will be filled later when the time is right. We
     * continue this process for all our "public" members.
     */
    /* Public OpMode members. */
    Gyroscope imu;
    DcMotor myBigMotorFrontLeft = null;
    DcMotor myBigMotorFrontRight = null;
    DcMotor myBigMotorBackLeft = null;
    DcMotor myBigMotorBackRight = null;
    DcMotor myBigMotorRandP = null;
    DcMotor myBigMotorLeftLifter = null;
    DcMotor myBigMotorRightLifter = null;

    CRServo myBigServoClaw = null;
    CRServo myBigServoFoundation = null;

    /**
     * Here we can begin to declare our local or "private" object class members. These are the
     * members, or in our case the hardware components, that we do not want other classes to have
     * access to. Local or "private" members can only be accessed by this object class and none
     * other. This is useful for class members and variables that we do not want to be changed by
     * other classes in our package.
     */

    /**
     * The variable type "HardwareMap" comes from the import above "import com.qualcomm.robotcore.hardware.HardwareMap;"
     * It is used to read the Hardware Configuration that we created on the Robot Controller phone. We named
     * our variable hwMap.
     */
    private HardwareMap hwMap           =  null;

    /**
     * This is where we create our constructor. A constructor is a specific kind of function used when creating
     * an object class. A constructor pretty much does what it's name says. It constructs the object when you use it
     * in other parts of your project. You can have multiple constructors i.e. a constructor that requires different types
     * of parameters or not and they call will have the same name which is the name of the object class.
     */
    /* Constructor */
    greaseBBQLightning() {

    }

    public void setRackAndPinionHeight(int position, double motorPower){
        myBigMotorRandP.setTargetPosition(position);
        myBigMotorRandP.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        myBigMotorRandP.setPower(motorPower);
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
        myBigMotorRandP = hwMap.get(DcMotor.class, "myBigMotorRandP");
        myBigMotorLeftLifter = hwMap.get(DcMotor.class, "myBigMotorLeftLifter");
        myBigMotorRightLifter = hwMap.get(DcMotor.class, "myBigMotorRightLifter");

        // Set all motors to zero power
        myBigMotorBackLeft.setPower(0);
        myBigMotorBackRight.setPower(0);
        myBigMotorFrontLeft.setPower(0);
        myBigMotorFrontRight.setPower(0);
        myBigMotorRandP.setPower(0);
        myBigMotorLeftLifter.setPower(0);
        myBigMotorRightLifter.setPower(0);

        // Set Direction of motors
        myBigMotorBackLeft.setDirection(DcMotor.Direction.FORWARD);
        myBigMotorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        myBigMotorBackRight.setDirection(DcMotor.Direction.REVERSE);
        myBigMotorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        myBigMotorRandP.setDirection(DcMotor.Direction.REVERSE);
        myBigMotorLeftLifter.setDirection(DcMotor.Direction.FORWARD);
        myBigMotorRightLifter.setDirection(DcMotor.Direction.REVERSE);

        myBigMotorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        myBigMotorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        myBigMotorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        myBigMotorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.

        //myBigMotorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //myBigMotorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //myBigMotorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //myBigMotorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //myBigMotorRandP.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //myBigMotorLeftLifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //myBigMotorRightLifter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize servos
        myBigServoClaw = hwMap.get(CRServo.class, "myBigServoClaw");
        myBigServoFoundation = hwMap.get(CRServo.class, "myBigServoFoundation");

        //Set all servos to power 0
        myBigServoClaw.setPower(0);
        myBigServoFoundation.setPower(0);

    }
}