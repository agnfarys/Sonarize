import { spotify } from "../assets";

const SignUp = () => {
  return (
    <div class="bg-landing w-full h-screen">
      <div className="flex items-center justify-center min-h-screen flex-col">
        <div className="tile backdrop-blur-sm p-8 rounded-xl w-[30rem] h-[33rem] shadow-xl">
          <h2 className="login text-white font-krona text-2xl mb-6 text-center">
            create your account{" "}
          </h2>

          <form className="font-raleway text-white space-y-6">
            <div>
              Email adress
              <input
                type="email"
                placeholder="e. g. youremail@gmail.com"
                className="insert w-full p-3 rounded-lg text-white placeholder:text-white/50 focus:outline-none focus:border-sonarizeAccent"
              />
            </div>

            <div>
              Password
              <input
                type="password"
                placeholder="***********"
                className="insert w-full p-3 mb-6 rounded-lg text-white placeholder:text-white/50 focus:outline-none focus:border-sonarizeAccent"
              />
            </div>

            <button
              type="submit"
              className="submit-button w-full p-3 rounded-lg font-krona transition-ease"
            >
              Sign up
            </button>

            <div className="text-center">or</div>

            <button
              type="button"
              className="spotify-button w-full p-3 rounded-lg text-white font-krona transition-colors flex items-center justify-center gap-2"
            >
              sign up with Spotify
              <img src={spotify} alt="Spotify" className="w-6 h-6 mx-3" />
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default SignUp;
