package xyz.jmullin

import com.badlogic.gdx.audio.{Music, Sound}
import com.badlogic.gdx.graphics.{Texture, Color, Pixmap}
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.g2d.{TextureAtlas, Sprite}
import xyz.jmullin.drifter.assets.DrifterAssets

object Assets extends DrifterAssets {
  var lightsaber: TextureAtlas = _

  var hiltOn: Sprite = _
  var hiltOff: Sprite = _

  var wall: Texture = _

  var hum: Music = _

  var on: Sound = _
  var on2: Sound = _
  var off: Sound = _
  var swing1: Sound = _
  var swing2: Sound = _
  var swing3: Sound = _
  var swing4: Sound = _
  var swing5: Sound = _
  var swing6: Sound = _
  var swing7: Sound = _
  var swing8: Sound = _

  val pixmap = new Pixmap(1, 1, Format.RGBA8888)
  pixmap.setColor(Color.WHITE)
  pixmap.fill()
  lazy val fill = new Sprite(new Texture(pixmap))
}
