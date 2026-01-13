import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import loginImage from "@/login.png"

const login = (provider: "github" | "google") => {
    const backend =
        window.location.host === "localhost:5173"
            ? "http://localhost:8080"
            : window.location.origin;

    window.location.href = `${backend}/oauth2/authorization/${provider}`;
};


export function LoginForm({
                              className,
                              ...props
                          }: React.ComponentProps<"div">) {
    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>
            <Card className="overflow-hidden">
                <CardContent className="grid p-0 md:grid-cols-2">
                    <div className="p-6 md:p-8 flex flex-col gap-6">
                        <div className="flex flex-col items-center text-center">
                            <h1 className="text-2xl font-bold">Einloggen</h1>
                            <p className="text-balance text-muted-foreground">
                                Melden Sie sich mit einem Ihrer Konten an
                            </p>
                        </div>
                        <div className="flex flex-col gap-4 items-center">
                            {/* Google */}
                            <Button
                                variant="outline"
                                className="w-full max-w-sm h-16"
                                onClick={() => login("google")}
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                                    <path
                                        d="M12.48 10.92v3.28h7.84c-.24 1.84-.853 3.187-1.787 4.133-1.147 1.147-2.933 2.4-6.053 2.4-4.827 0-8.6-3.893-8.6-8.72s3.773-8.72 8.6-8.72c2.6 0 4.507 1.027 5.907 2.347l2.307-2.307C18.747 1.44 16.133 0 12.48 0 5.867 0 .307 5.387.307 12s5.56 12 12.173 12c3.573 0 6.267-1.173 8.373-3.36 2.16-2.16 2.84-5.213 2.84-7.667 0-.76-.053-1.467-.173-2.053H12.48z"
                                        fill="currentColor"
                                    />
                                </svg>
                                <span className="ml-2">Mit Google anmelden</span>
                            </Button>

                            {/* GitHub */}
                            <Button
                                variant="outline"
                                className="w-full max-w-sm h-16"
                                onClick={() => login("github")}
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                                    <path
                                        d="M12 0C5.37 0 0 5.37 0 12c0 5.3 3.438 9.8 8.205 11.385.6.113.82-.26.82-.577 0-.285-.01-1.04-.015-2.04-3.338.724-4.042-1.61-4.042-1.61-.546-1.387-1.333-1.756-1.333-1.756-1.09-.745.083-.73.083-.73 1.205.085 1.838 1.237 1.838 1.237 1.07 1.835 2.807 1.305 3.492.997.108-.775.418-1.305.762-1.605-2.665-.305-5.467-1.333-5.467-5.932 0-1.31.468-2.38 1.236-3.22-.124-.303-.536-1.523.117-3.176 0 0 1.008-.322 3.3 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.287-1.552 3.292-1.23 3.292-1.23.655 1.653.243 2.873.12 3.176.77.84 1.235 1.91 1.235 3.22 0 4.61-2.807 5.625-5.48 5.922.43.372.823 1.102.823 2.222 0 1.605-.015 2.897-.015 3.293 0 .32.216.694.825.576C20.565 21.796 24 17.298 24 12c0-6.63-5.37-12-12-12z"
                                        fill="currentColor"
                                    />
                                </svg>
                                <span className="ml-2">Mit GitHub anmelden</span>
                            </Button>

                        </div>
                    </div>

                    <div className="relative hidden md:block w-full h-full">
                        <img
                            src={loginImage}
                            alt="Login Bild"
                            className="w-full h-full object-cover dark:brightness-[0.2] dark:grayscale"
                        />
                    </div>
                </CardContent>
            </Card>
        </div>
    )
}
