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
    let chat = ChatService(host: "192.168.2.127", port: 8081, name: "iOSUser")
    @State var messages = ""
    @State var fieldText = ""
    var body: some View {
        VStack {
            Text(messages)
            HStack {
                TextField("Message",text: $fieldText)
                Button("send", action: {
                    self.chat.send(msg: self.fieldText)
                    self.fieldText = ""
                })
            }.padding()
        }.onAppear {
            self.chat.onMessage = {msg in
                self.messages += msg.user + ": " + msg.content + "\n"
            }
            self.chat.onReady = {
                self.chat.send(msg: "iOS User Join")
            }
            self.chat.connect()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
