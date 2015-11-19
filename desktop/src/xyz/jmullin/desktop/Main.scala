package xyz.jmullin.desktop

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import xyz.jmullin.Application

object Main extends App {
  val config = new LwjglApplicationConfiguration

  config.title = "Lightsaber"
  config.width = 800
  config.height = 600

  new LwjglApplication(new Application, config)
}