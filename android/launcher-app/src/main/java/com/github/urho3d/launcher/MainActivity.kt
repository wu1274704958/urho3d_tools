//
// Copyright (c) 2008-2020 the Urho3D project.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//

package com.github.urho3d.launcher

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.github.urho3d.UrhoActivity

class MainActivity : UrhoActivity() {

    companion object {
        const val argument = "argument"
    }

    private lateinit var arguments: List<String>

    override fun getArguments() = arguments.toTypedArray()

    override fun onLoadLibrary(libraryNames: MutableList<String>) {
        // All runtime shared libraries must always be loaded if available
        val regex = Regex("^(?:Urho3D|.+_shared)\$")
        libraryNames.retainAll { regex.matches(it) }

        // Parse the argument string
        val argumentString = intent.getStringExtra(argument)
        if (argumentString != null) arguments = argumentString.split(':')
        else throw IllegalArgumentException("The MainActivity requires an argument to start")

        // Must add the chosen sample library to the last of the list
        libraryNames.add(arguments[0])

        super.onLoadLibrary(libraryNames)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("===","onCreate()")
        window.decorView
            .setOnSystemUiVisibilityChangeListener { setHideVirtualKey(window) }

    }

    fun setHideVirtualKey(window: Window) {
        //保持布局状态
        Log.e("===","setHideVirtualKey()");
        var uiOptions: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        uiOptions = if (Build.VERSION.SDK_INT >= 19) {
            uiOptions or 0x00001000
        } else {
            uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
        window.getDecorView().setSystemUiVisibility(uiOptions)
    }

}
