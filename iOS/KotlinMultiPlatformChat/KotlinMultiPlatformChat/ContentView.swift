//
//  ContentView.swift
//  KotlinMultiPlatformChat
//
//  Created by 市川創大 on 2020/07/24.
//  Copyright © 2020 市川創大. All rights reserved.
//

import SwiftUI
import SharedCode

final class Result: ObservableObject {
    @Published var result: String = "loading"
    func load() {
        CommonKt.fetchRemoteMessage { (res: String) in
            self.result = res
        }
    }
}

struct ContentView: View {
    @ObservedObject var data = Result()
    var body: some View {
        Text(data.result).onAppear {
            self.data.load()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
