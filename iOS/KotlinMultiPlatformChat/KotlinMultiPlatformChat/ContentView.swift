//
//  ContentView.swift
//  KotlinMultiPlatformChat
//
//  Created by 市川創大 on 2020/07/24.
//  Copyright © 2020 市川創大. All rights reserved.
//

import SwiftUI
import SharedCode

struct ContentView: View {
    var body: some View {
        Text(CommonKt.createApplicationScreenMessage())
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
