package xyz.jmullin

import com.badlogic.gdx.{Gdx, Input}
import xyz.jmullin.drifter.DrifterScreen
import xyz.jmullin.drifter.GdxAlias._
import xyz.jmullin.drifter.enrich.RichGeometry.V2

object MainScreen extends DrifterScreen {
  val world = newLayer2D(gameSize, true)

  world.add(Backdrop)
  world.add(Lightsaber)

  override def touchDown(v: V2, pointer: Int, button: Int) = {
    if(!Gdx.input.isCursorCatched) {
      Gdx.input.setCursorCatched(true)
    }

    super.touchDown(v, pointer, button)
  }

  override def keyDown(keycode: Int) = {
    if(keycode == Input.Keys.ESCAPE) {
      if(Gdx.input.isCursorCatched) {
        Gdx.input.setCursorPosition(gameW/2, gameH/2)
        Gdx.input.setCursorCatched(false)
      }
    }

    super.keyDown(keycode)
  }
}
