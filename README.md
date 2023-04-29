# LiquidX
A free minecraft client based on LiquidBounce, supporting forge version 1.8.9

Website: https://liquidxclient.github.io/

YouTube: https://www.youtube.com/channel/UCehNLABEXtPuCZOMJQnClug

Discord: https://discord.gg/pZnQECzmub


## License
This project is subject to the [GNU General Public License v3.0](LICENSE). This does only apply for source code located directly in this clean repository. During the development and compilation process, additional source code may be used to which we have obtained no rights. Such code is not covered by the GPL license.

For those who are unfamiliar with the license, here is a summary of its main points. This is by no means legal advice nor legally binding.

You are allowed to
- use
- share
- modify

this project entirely or partially for free and even commercially. However, please consider the following:

- **You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.**
- **Your modified application must also be licensed under the GPL**


## Setting up a Workspace
LiquidX uses Gradle, to make sure that it is installed properly you can check [Gradle's website](https://gradle.org/install/).
1. Clone the repository using `git clone https://github.com/PrahXZ/LiquidX.git`. 
2. CD into the local repository folder.
3. If you are using Intelij run the following command `gradlew setupDevWorkspace idea genIntellijRuns build`
4. Open the folder as a Gradle project in your IDE. (Make sure that your IDE is using Java 8, if not then it will have issues)
5. Select and click "LiquidX.ipr".

