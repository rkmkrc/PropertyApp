import { NextResponse } from 'next/server';
import { setCookie } from 'nookies';

export async function POST(request: Request) {
  const formData = await request.formData();
  const name = formData.get('name') as string;
  const surname = formData.get('surname') as string;
  const email = formData.get('email') as string;
  const password = formData.get('password') as string;

  if (!name || !surname || !email || !password) {
    return NextResponse.json({ success: false, message: 'All fields are required' }, { status: 400 });
  }

  const backendRegisterUrl = `http://localhost:8080/api/v1/auth/register`;

  const response = await fetch(backendRegisterUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ name, surname, email, password }),
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
    return NextResponse.json({ success: true, message: 'Registration successful' });
  } else {
    return NextResponse.json({ success: false, message: data.message || 'Registration failed' }, { status: response.status });
  }
}
