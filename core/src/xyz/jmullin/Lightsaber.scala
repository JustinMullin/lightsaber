package xyz.jmullin

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{Color, GL20}
import com.badlogic.gdx.{Gdx, Input}
import xyz.jmullin.drifter.FloatMath._
import xyz.jmullin.drifter.GdxAlias._
import xyz.jmullin.drifter.RandomUtil._
import xyz.jmullin.drifter.animation.Trigger._
import xyz.jmullin.drifter.enrich.RichColor._
import xyz.jmullin.drifter.enrich.RichGeometry._
import xyz.jmullin.drifter.entity.{Entity2D, EntityContainer2D}
import xyz.jmullin.drifter.{Draw, ShaderSet, Shaders}

object Lightsaber extends Entity2D {
  lazy val hiltSize = V2(Assets.hiltOff.getWidth, Assets.hiltOff.getHeight) / 4f

  val bladeColors = List(Color.BLUE, Color.GREEN, Color.RED)

  val bladeLength = 250f
  val bladeWidth = 10f
  val extendTime = 0.1f
  val retractTime = 0.2f
  val bladeColor = C(0, 0, 1)

  val bladePosition = V2(0, 0)

  lazy val swings = List(
    Assets.swing1,
    Assets.swing2,
    Assets.swing3,
    Assets.swing4,
    Assets.swing5,
    Assets.swing6,
    Assets.swing7,
    Assets.swing8)

  var tilt = 0f
  var extend = 0f
  var on = false

  var buzzDelay = 0f

  lazy val shader = new ShaderSet("saber", "saber")

  override def create(container: EntityContainer2D): Unit = {
    position.set(gameSize/2f)
    Assets.hum.setVolume(0f)
    Assets.hum.setLooping(true)
    Assets.hum.play()
  }

  override def render(implicit batch: SpriteBatch): Unit = {
    Assets.hiltOff.setOriginCenter()
    Assets.hiltOff.setRotation(tilt)
    Assets.hiltOff.setColor(Ci(0.2f+extend*0.8f))
    Draw.sprite(Assets.hiltOff, position-hiltSize/2f, hiltSize)

    val bladeNormal = V2(0, 1f).rotate(tilt)
    val bladeStart = position + bladeNormal * (hiltSize.y/2f)
    val bladeEnd = bladeStart + bladeNormal * extend * bladeLength

    bladePosition.set((bladeStart + bladeEnd) / 2f)

    Shaders.switch(shader)(layer.get, batch)
    //shader.program.begin()

    shader.program.setUniformf("bladeStart", bladeStart)
    shader.program.setUniformf("bladeEnd", bladeEnd)
    shader.program.setUniformf("bladeColor", bladeColor)

    batch.enableBlending()
    batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE)

    Assets.fill.setColor(bladeColor)
    Assets.fill.setRotation(tilt)
    Assets.fill.setOrigin(bladeWidth*2f, bladeLength*0.05f)
    Draw.sprite(Assets.fill, bladeStart-V2(bladeWidth*8f/2, bladeLength*0.25f), V2(bladeWidth*8f, bladeLength*extend*1.5f))

    batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

    //shader.program.end()
    Shaders.switch(Shaders.default)(layer.get, batch)
  }

  override def update(implicit delta: Float): Unit = {
    Assets.hum.setVolume(extend*0.2f)

    if(Gdx.input.isCursorCatched) {
      position.set(mouseV)

      tilt -= mouseVelocity.x/10f
      tilt += (0-tilt)/10f

      if(mouseVelocity.len() > 1f && on && extend >= 0.99f) {
        if(buzzDelay <= 0f) {
          buzzDelay = 0.25f
          val vol = max(0f, min(1f, pow(mouseVelocity.len() / 10f, 2)))
          rElement(swings).play(vol)
        }
      }

      buzzDelay -= delta
    } else if(on) {
      turnOff()
    }

    super.update(delta)
  }

  override def containsPoint(v: V2) = true

  override def touchDown(v: V2, pointer: Int, button: Int) = {
    button match {
      case 0 if !on && extend <= 0.01f =>
        turnOn()
      case 1 if on && extend >= 0.99f =>
        turnOff()
      case _ => Unit
    }

    true
  }

  def turnOn(): Unit = {
    on = true
    tween(extendTime) { n =>
      extend = n
    } go()

    if(probability(0.5f)) {
      Assets.on.play()
    } else {
      Assets.on2.play()
    }
  }

  def turnOff(): Unit = {
    on = false
    tween(retractTime) { n =>
      extend = 1f - n
    } go()
    Assets.on.stop()
    Assets.on2.stop()
    Assets.off.play(0.7f)
  }

  override def keyDown(keycode: Int): Boolean = {
    if(keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_3) {
      bladeColor.set(bladeColors(keycode-Input.Keys.NUM_1))
    }

    true
  }
}
