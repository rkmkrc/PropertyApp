import { NextResponse } from 'next/server';
import { setCookie } from 'nookies';

export async function POST(request: Request) {
  const formData = await request.formData();
  const email = formData.get('email') as string;
  const password = formData.get('password') as string;

  const response = await fetch(`http://localhost:8080/api/v1/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });

  const data = await response.json();

  if (response.ok) {
    const res = NextResponse.json({ success: true, message: 'Login successful' });
    setCookie({ res }, 'token', data.token, {
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      maxAge: 30 * 24 * 60 * 60,
      path: '/',
    });
    return res;
  } else {
    return NextResponse.json({ success: false, message: data.message }, { status: response.status });
  }
}
