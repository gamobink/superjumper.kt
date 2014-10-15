/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicgames.superjumper

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

public class MainMenuScreen(var game: SuperJumper) : ScreenAdapter() {
    var guiCam: OrthographicCamera
    var soundBounds: Rectangle
    var playBounds: Rectangle
    var highscoresBounds: Rectangle
    var helpBounds: Rectangle
    var touchPoint: Vector3

    {

        guiCam = OrthographicCamera(320f, 480f)
        guiCam.position.set((320 / 2).toFloat(), (480 / 2).toFloat(), 0f)
        soundBounds = Rectangle(0f, 0f, 64f, 64f)
        playBounds = Rectangle((160 - 150).toFloat(), (200 + 18).toFloat(), 300f, 36f)
        highscoresBounds = Rectangle((160 - 150).toFloat(), (200 - 18).toFloat(), 300f, 36f)
        helpBounds = Rectangle((160 - 150).toFloat(), 200 - 18 - 36.toFloat(), 300f, 36f)
        touchPoint = Vector3()
    }

    public fun update() {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat(), 0f))

            if (playBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound)
                game.setScreen(GameScreen(game))
                return
            }
            if (highscoresBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound)
                game.setScreen(HighscoresScreen(game))
                return
            }
            if (helpBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound)
                game.setScreen(
                        HelpScreen(game, "data/help1.png",
                                HelpScreen(game, "data/help2.png",
                                        HelpScreen(game, "data/help3.png",
                                                HelpScreen(game, "data/help4.png",
                                                        HelpScreen(game, "data/help5.png", this)))))
                )
                return
            }
            if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound)
                Settings.soundEnabled = !Settings.soundEnabled
                if (Settings.soundEnabled)
                    Assets.music.play()
                else
                    Assets.music.pause()
            }
        }
    }

    public fun draw() {
        val gl = Gdx.gl
        gl.glClearColor(1f, 0f, 0f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        guiCam.update()
        game.batcher.setProjectionMatrix(guiCam.combined)

        game.batcher.disableBlending()
        game.batcher.begin()
        game.batcher.draw(Assets.backgroundRegion, 0f, 0f, 320f, 480f)
        game.batcher.end()

        game.batcher.enableBlending()
        game.batcher.begin()
        game.batcher.draw(Assets.logo, (160 - 274 / 2).toFloat(), 480 - 10 - 142.toFloat(), 274f, 142f)
        game.batcher.draw(Assets.mainMenu, 10f, (200 - 110 / 2).toFloat(), 300f, 110f)
        game.batcher.draw(if (Settings.soundEnabled) Assets.soundOn else Assets.soundOff, 0f, 0f, 64f, 64f)
        game.batcher.end()
    }

    override fun render(delta: Float) {
        update()
        draw()
    }

    override fun pause() {
        Settings.save()
    }
}
