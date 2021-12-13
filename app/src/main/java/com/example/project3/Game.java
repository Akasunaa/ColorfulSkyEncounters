package com.example.project3;


import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Game extends View {

    Context context;
    Bitmap background, lifeImage,blueButton,redButton,yellowButton,greenButton;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_SIZE = 80;
    boolean paused = false;
    Player player;
    Random random;
    MyDatabase mydb;
    ArrayList<Shot> playerShots;
    ArrayList<EnemySpaceship> enemySpaceships;
    ArrayList<EnemySpaceship> enemySpaceshipsOnPlay;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    float cooldownShot = 0;
    float cooldownMonster=0;
    float cooldownLife=0;
    boolean canShoot = true;
    boolean canSpawn=true;
    boolean canLooseLife=true;


    int boucle=0;
    BackgroundSound mBackgroundSound;


    public Game(Context context) {
        super(context);
        this.context = context;

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        playerShots = new ArrayList<>();
        enemySpaceships = new ArrayList<>();
        enemySpaceshipsOnPlay = new ArrayList<>();
        player = new Player(context);
        //enemySpaceship = new EnemySpaceship(context);
        handler = new Handler();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.mybackground);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        lifeImage=lifeImage.createScaledBitmap(lifeImage,80,80,true);
        blueButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.boutonblue);
        redButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.boutonrouge);
        greenButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.boutongreen);
        yellowButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.boutonyellow);
        scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        mydb = new MyDatabase(context);
        mBackgroundSound = new BackgroundSound();
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //On instencie la liste des ennemis
        //faire des randoms pour les couleurs
        //les dessiner après

        //je te promet un jour je le ferai dans l'asynctask <3
        String[] randomColor = new String[]{"red","blue","green","yellow"};

        for (int i=0;i<10;i++){
            int random=new Random().nextInt(4);
            EnemySpaceship enemySpaceship = new EnemySpaceship(context,randomColor[random]);
            enemySpaceships.add(enemySpaceship);
        }
        mBackgroundSound.execute((Void) null);


    }


    //Probleme : je redessine les boutons chaque frame. C'est pas ouf.
    @Override
    protected void onDraw(Canvas canvas) {
        //GROS CA NA AUCUN SENS JE SORS CETTE LIGNE DE MON TROUDUC SERIEUX mais camarsh
        this.getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        if(paused){
            mBackgroundSound.cancel(true);
        }
        //jdessine des trucs tkt
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawText("Pt: " + points,0,150,scorePaint);
        for(int i=life; i>=1; i--){
            canvas.drawBitmap(lifeImage, lifeImage.getWidth() * (i-1), 0, null);

        }

        //les boutons de couleurs lets go
        canvas.drawBitmap(blueButton, 0, 1700, null);
        canvas.drawBitmap(redButton, 0, 1700-redButton.getWidth()-50, null);
        canvas.drawBitmap(yellowButton, 0, 1700-blueButton.getWidth()*2-100, null);
        canvas.drawBitmap(greenButton, 0, 1700-blueButton.getWidth()*3-150, null);


        // Vie nulle, changement sur l'activité game over
        if(life <= 0){
            mBackgroundSound.cancel(true);
            String id = ((Activity) context).getIntent().getExtras().getString("id");
            mydb.update(id,points);
            mydb.readData();
            paused = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            mBackgroundSound.cancel(true);
            //intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        //gere mes ennemi en jeux
        if(canSpawn){
            if (boucle>enemySpaceships.size()-2){
                boucle=0;
            }
            boucle+=1;
            Log.i("nombre", String.valueOf(boucle));
            EnemySpaceship enemy= enemySpaceships.get(boucle);
            enemy.reset();
            enemySpaceshipsOnPlay.add(enemy);
            cooldownMonster=2000f;
            canSpawn=false;

        }

        //todo: metttre dans une asynctask
        //gere les ennemis en jeu
        //boucle for de la mort jpp
        for(int i=0;i<enemySpaceshipsOnPlay.size();i++) {
            EnemySpaceship enemySpaceship;

            enemySpaceship = enemySpaceshipsOnPlay.get(i);


            // Move enemySpaceship
            enemySpaceship.ey += enemySpaceship.enemyVelocity;
            if (enemySpaceship.ey>2150){
                enemySpaceshipsOnPlay.remove(i);
            }
            //chock entre player et ennemy
            if (((enemySpaceship.ey >= player.ox)
                    && enemySpaceship.ex <= player.ox + player.getOurSpaceshipWidth()
                    && enemySpaceship.ey >= player.oy
                    && enemySpaceship.ey <= screenHeight
                    && canLooseLife)) {
                life--;

                @SuppressLint("WrongConstant") Toast toast = Toast.makeText(context, "Oh no!", 10);
                toast.show();
                enemySpaceships.remove(i);
                cooldownLife=500f;
                canLooseLife=false;


            }


            //Dessine l'ennemi
            canvas.drawBitmap(enemySpaceship.bitmapEnemy, enemySpaceship.ex, enemySpaceship.ey, null);
            Rect rectEnemy = new Rect(enemySpaceship.ex, enemySpaceship.ey, enemySpaceship.ex + 200, enemySpaceship.ey + 200);
            canvas.drawPicture(enemySpaceship.svgEnemy.getPicture(), rectEnemy); //dessine svg

            //Tape l'ennemi
            for(int j = 0; j < playerShots.size(); j++){
                playerShots.get(j).shy -= 15;
                canvas.drawBitmap(playerShots.get(j).getShot(), playerShots.get(j).shx, playerShots.get(j).shy, null);
                if((playerShots.get(j).shx >= enemySpaceship.ex)
                        && playerShots.get(j).shx <= enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth()
                        && playerShots.get(j).shy <= enemySpaceship.ey+enemySpaceship.getEnemySpaceshipWidth()
                        && playerShots.get(j).shy >= enemySpaceship.ey
                        && playerShots.get(j).color==enemySpaceship.color){
                    points++; //a changer
                    playerShots.remove(j);
                    enemySpaceshipsOnPlay.remove(i);



                }else if(playerShots.get(j).shy <=0){
                    playerShots.remove(j);
                }
            }
        }

        //Limite de déplacement du joueur
        if(player.ox > screenWidth - player.getOurSpaceshipWidth()){
            player.ox = screenWidth - player.getOurSpaceshipWidth();
        }else if(player.ox < blueButton.getWidth()){
            player.ox = blueButton.getWidth();
        }

        //Dessine le player
        canvas.drawBitmap(player.bitmapPlayer, player.ox, player.oy, null);
        Rect rectPlayer = new Rect(player.ox, player.oy, player.ox+200, player.oy+200);
        canvas.drawPicture(player.svgPlayer.getPicture(),rectPlayer); //dessine svg




        //update à la mano et les cooldown qui vont avec
        if(!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);
            cooldownShot -=UPDATE_MILLIS;
            cooldownMonster-=UPDATE_MILLIS;
            cooldownLife-=UPDATE_MILLIS;
            //cooldown du tir
            if (cooldownShot <=0){
                canShoot=true;
            }
            if(cooldownMonster<=0){
                canSpawn=true;
            }
            if(cooldownLife<=0){
                canLooseLife=true;
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //ou jappui
        int touchX = (int)event.getX();
        int touchY = (int)event.getY();

        //choisir la couleur et shoot avec (3 : nb projectil en meme temps)
        //bleu
        if(canShoot && event.getAction() == MotionEvent.ACTION_DOWN && touchY>1700 && touchX<blueButton.getWidth() && touchY<1700+blueButton.getHeight()){
            {
                Shot ourShot = new Shot(context, player.ox + player.getOurSpaceshipWidth() / 2, player.oy,"blue");
                playerShots.add(ourShot);
                //cooldown
                cooldownShot =500f;
                canShoot=false;
            }
        }
        //rouge
        if(canShoot && event.getAction() == MotionEvent.ACTION_DOWN && touchY>1700-redButton.getWidth()-50 && touchX<blueButton.getWidth() && touchY<1700-blueButton.getWidth()-100+blueButton.getHeight()){
            {
                Shot ourShot = new Shot(context, player.ox + player.getOurSpaceshipWidth() / 2, player.oy,"red");
                playerShots.add(ourShot);
                //cooldown
                cooldownShot =500f;
                canShoot=false;
            }
        }
        //yellow
        if(canShoot && event.getAction() == MotionEvent.ACTION_DOWN && touchY>1700-blueButton.getWidth()*2-100 && touchX<blueButton.getWidth() && touchY<1700-blueButton.getWidth()*2-100+blueButton.getHeight()){
            if(playerShots.size() < 5){
                Shot ourShot = new Shot(context, player.ox + player.getOurSpaceshipWidth() / 2, player.oy,"yellow");
                playerShots.add(ourShot);
                //cooldown
                cooldownShot =500f;
                canShoot=false;
            }
        }
        //Vert
        if(canShoot && event.getAction() == MotionEvent.ACTION_DOWN && touchY>1700-blueButton.getWidth()*3-150 && touchX<blueButton.getWidth() && touchY<1700-blueButton.getWidth()*2-100+blueButton.getHeight()){
            if(playerShots.size() < 5){
                Shot ourShot = new Shot(context, player.ox + player.getOurSpaceshipWidth() / 2, player.oy,"green");
                playerShots.add(ourShot);
                //cooldown
                cooldownShot =500f;
                canShoot=false;
            }
        }



        //Bouger le player
        if(event.getAction() == MotionEvent.ACTION_MOVE && touchX>200){
            player.ox = touchX;
        }


        return true;
    }

    public class BackgroundSound extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            MediaPlayer player = MediaPlayer.create(((Activity) context), R.raw.spacetheme);
            player.setLooping(true); // Set looping
            player.setVolume(1.0f, 1.0f);
            player.start();
            return null;
        }

    }


}
