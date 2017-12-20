/**
 * @file EtRobocon2017.h
 * @brief main的なクラス
 * @author Futa HIRAKOBA
 */
#ifndef __ETROBOCON2017__
#define __ETROBOCON2017__

#include "ev3api.h"
#include "TouchSensor.h"
#include "SonarAlert.h"
#include "Lifter.h"
#include "Emoter.h"
#include "UserInterface.h"
#include "LeftCourse.h"
#include "RightCourse.h"
#include "basicWalker.h"


/**
Helloooo
 */
using namespace ev3api;

class /**Test*/ EtRobocon2017 {
public:
    EtRobocon2017();
    /** タッチセンサが押されたときに行われる処理 */
    void start( int );
    //テスト
    void waitStarter( int );
    void loop(){
        int a=0;
    };

private:
    TouchSensor touchSensor;
    int8_t light_white;
    int8_t light_black;
    int32_t firstCode;
    LeftCourse leftCourse;    
    RightCourse rightCourse;
    UserInterface ui;
};

#endif
