package xyz.jmullin

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import xyz.jmullin.drifter.GdxAlias._
import xyz.jmullin.drifter.enrich.RichGeometry._
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.{Draw, ShaderSet, Shaders}

object Backdrop extends Entity2D {
  depth = 100

  lazy val textureSize = V2(Assets.wall.getWidth, Assets.wall.getHeight)

  lazy val shader = new ShaderSet("wall", "default")

  override def render(implicit batch: SpriteBatch): Unit = {
    Shaders.switch(shader)(layer.get, batch)

    shader.program.setUniformf("bladePosition", Lightsaber.bladePosition)
    shader.program.setUniformf("bladePower", Lightsaber.extend)
    shader.program.setUniformf("bladeColor", Lightsaber.bladeColor)

    Draw.texture(Assets.wall, gameSize/2f - textureSize / 2f, textureSize)

    Shaders.switch(Shaders.default)(layer.get, batch)
  }
}
