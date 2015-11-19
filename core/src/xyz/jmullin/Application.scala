package xyz.jmullin

import com.badlogic.gdx.{Gdx, Game}

class Application extends Game {
  override def create(): Unit = {
    Assets.load()
    Assets.finishLoading()
    Assets.populate()

    Gdx.input.setCursorCatched(true)

    setScreen(MainScreen)
  }
}
