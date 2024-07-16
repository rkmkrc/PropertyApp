import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';
import { setCookie } from 'nookies';

export async function POST(request: Request) {
  const formData = await request.formData();
  const email = formData.get('email') as string;
  const password = formData.get('password') as string;

  if (!email || !password) {
    return NextResponse.json({ success: false, message: 'All fields are required' }, { status: 400 });
  }

  const backendLoginUrl = `http://localhost:8080/api/v1/auth/login`

  const response = await fetch(backendLoginUrl, {
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
    return NextResponse.json({ success: false, message: data.message || 'Login failed'}, { status: response.status });
  }
}
