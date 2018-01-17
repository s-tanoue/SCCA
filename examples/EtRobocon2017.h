/**
 * @file EtRobocon2017.h
 * @brief main的なクラス
 * @author Futa HIRAKOBA
 */
#ifndef __ETROBOCON2017__
#define __ETROBOCON2017__
#include "ev3api.h"


int a; //aに対するコメント
/*bに対するコメント*/
int b;
using namespace ev3api;

class EtRobocon2017 {
public:
    EtRobocon2017();
    void start( int ); //startに対するコメント
    /* waitStarter */
    void waitStarter( int );
private:
    TouchSensor touchSensor;
    int8_t light_white;
};

#endif
